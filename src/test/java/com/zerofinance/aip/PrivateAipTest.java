package com.zerofinance.aip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.aeasycredit.commons.json.JsonUtils;
import com.google.common.collect.Lists;
import com.zerofinance.aip.enums.FaceTypeEnum;
import com.zerofinance.aip.model.MatchInfo;
import com.zerofinance.aip.service.AipService;

/**
 * PrivateAipTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AipApplication.class)
public class PrivateAipTest {

    @Autowired
    public AipService aipService;

    private void writeLog(String log, boolean append) {
        File file = new File("/home/dave/ai.log");
        try {
            FileUtils.write(file, log + "\n", append);
			System.out.println(log);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String getFileMd5(Path path) throws IOException {
    	System.out.println("md5 start--->"+LocalDateTime.now().toString());
    	InputStream is = Files.newInputStream(path);
    	try {
    	    String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        	System.out.println("md5 end--->"+LocalDateTime.now().toString());
    	    return md5;
    	} finally {
    		is.close();
    	}
    }

    // @Test
    public void importAllPic() throws IOException {
    	writeLog("start--->"+LocalDateTime.now().toString(), false);
    	// 创建group
    	String groupId1 = "xwallet_id_card";
    	String result = aipService.addGroup(groupId1);
    	if(result.indexOf("SUCCESS") == -1) {
        	writeLog(result, true);
    	}
    	writeLog("GroupId1--->"+groupId1, true);
    	String groupId2 = "xwallet_live_photo";
    	result = aipService.addGroup(groupId2);
    	if(result.indexOf("SUCCESS") == -1) {
        	writeLog(result, true);
    	}
    	writeLog("GroupId2--->"+groupId2, true);
        String path = "/Developer/users/";
//        String path = "/home/dave/test/";
        Path dir = Paths.get(path);
        int idCardCount = 0;
        int livePhotoCount = 0;
        int errCount = 0;
        List<String> idCardLists = Lists.newArrayList();
        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
        try {
            for(Path e : stream){
//            	String userRootId = e.toFile().getName();
            	String userId = e.toFile().getName();
            	writeLog("userId--->"+userId, true);
            	List<Path> files = Files.list(e).collect(Collectors.toList());
            	for(Path f : files) {
            		String fileName = f.toFile().getName();
            		String md5 = getFileMd5(f);
//            		System.out.println("md5--->"+md5);
            		if(idCardLists.contains(md5)) {
            			writeLog(fileName+", md5: "+md5+" existed!", true);
            			errCount++;
            		} else {
                		idCardLists.add(md5);
                		if(fileName.startsWith("Idcard-")) {
                        	String idCard = f.toFile().getName();
                            InputStream input = null;
                            try {
//                            	String userId = userRootId+"_"+StringUtils.substringAfter(FilenameUtils.getBaseName(f.toFile().getPath()), "-");
//                            	writeLog("userId--->"+userId, true);
                            	writeLog("Idcard--->"+idCard, true);
                                input = new FileInputStream(f.toFile());
                                MultipartFile multipartFile = new MockMultipartFile(idCard, input);
                                String result1 = aipService.addFace(groupId1, userId, idCard, multipartFile);
                            	if(result1.indexOf("SUCCESS") == -1) {
                            		// error
                                	writeLog(result1, true);
                        			errCount++;
                            	} else {
                            		idCardCount++;
                            	}
                            } catch (IOException e1) {
    							e1.printStackTrace();
    							writeLog(e1.getMessage(), true);
    						} finally {
                            	IOUtils.closeQuietly(input);
                            }
                		} else if(fileName.startsWith("LivePhoto-")) {
                        	String livePhoto = f.toFile().getName();
                            InputStream input = null;
                            try {
//                            	String userId = userRootId+"_"+StringUtils.substringAfter(FilenameUtils.getBaseName(f.toFile().getPath()), "-");
//                            	writeLog("userId--->"+userId, true);
                            	writeLog("livePhoto--->"+livePhoto, true);
                                input = new FileInputStream(f.toFile());
                                MultipartFile multipartFile = new MockMultipartFile(livePhoto, input);
                                String result2 = aipService.addFace(groupId2, userId, livePhoto, multipartFile);
                            	if(result2.indexOf("SUCCESS") == -1) {
                            		// error
                                	writeLog(result2, true);
                        			errCount++;
                            	} else {
                            		livePhotoCount++;
                            	}
                            } catch (IOException e1) {
    							e1.printStackTrace();
    							writeLog(e1.getMessage(), true);
    						} finally {
                            	IOUtils.closeQuietly(input);
                            }
                		} else {
    						writeLog("===>"+f.getFileName(), true);
                		}
            		}
            	}
            }
        	writeLog("idCardCount--->"+idCardCount, true);
        	writeLog("livePhotoCount--->"+livePhotoCount, true);
        	writeLog("errCount--->"+errCount, true);
        	writeLog("end--->"+LocalDateTime.now().toString(), true);
        } catch(IOException e){
			writeLog(e.getMessage(), true);
        } finally {
        	stream.close();
        }
    }
    
    @Test
    public void matchTest() throws IOException {
        
        int tpCount = 0; // 正类判定为正类 
        int fnCount = 0; // 正类判定为负类
        
        MatchInfo matchInfo = new MatchInfo();
        matchInfo.setScoreIfSamePerson(80);
        matchInfo.setPositivesCount(50); 
        matchInfo.setNegativesCount(20);
        
        int positivesCount = 0; // 正類數
        
        // 准确率(Accuracy), 精确率(Precision), 召回率(Recall)和F1-Measure
        
        String dataPath = "D:\\users";
        Path dataDir = Paths.get(dataPath);
        DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir);
        try {
            for (Path e : stream) {
                
                if (positivesCount >= matchInfo.getPositivesCount()) {
                    break;
                }
                
                String userId = e.toFile().getName();
                
                List<Path> idCardPathList = Lists.newArrayList();
                List<Path> livePhotoPathList = Lists.newArrayList();
                
                List<Path> files = Files.list(e).collect(Collectors.toList());
                for(Path file : files) {
                    String fileName = file.toFile().getName();
                    if (StringUtils.startsWith(fileName, "Idcard-")) {
                        idCardPathList.add(file);
                    } else if (StringUtils.startsWith(fileName, "LivePhoto-")) {
                        livePhotoPathList.add(file);
                    }
                }
                
                // 隨機獲取一個身份證照片
                int index = new Random().nextInt(idCardPathList.size());
                Path idCardPath = idCardPathList.get(index);
                
                // 隨機獲取一個活體照片
                index = new Random().nextInt(livePhotoPathList.size());
                Path livePhotoPath = livePhotoPathList.get(index);
                
                String idCardFileName = idCardPath.toFile().getName();
                String livePhotoFileName = livePhotoPath.toFile().getName();
                
                InputStream idCardInput = null;
                InputStream livePhotoInput = null;
                try {
                    idCardInput = new FileInputStream(idCardPath.toFile());
                    MultipartFile idCardMultipartFile = new MockMultipartFile(idCardFileName, idCardInput);
                    
                    livePhotoInput = new FileInputStream(livePhotoPath.toFile());
                    MultipartFile livePhotoMultipartFile = new MockMultipartFile(livePhotoFileName, livePhotoInput);
                    
                    // FaceTypeEnum.LIVE --> FaceTypeEnum.CERT
                    String result = aipService.match(FaceTypeEnum.CERT, idCardMultipartFile, FaceTypeEnum.CERT, livePhotoMultipartFile);
                    if(result.indexOf("SUCCESS") == -1) {
                        // error
                        writeLog("userId:" + userId +" " + result, true);
                        //errCount++;
                    } else {
                        //writeLog(result, true);
                        //idCardCount++;
                        positivesCount ++;
                        
                        Map<String, Object> responseMap = JsonUtils.toMap(result);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> resultInResponseMap = (Map<String, Object>)responseMap.get("result");

                        Double score = this.getScoreFromMap(resultInResponseMap);
                        
                        String logInfo = "userId:" + userId + " score:" + score + " idcard:" + idCardFileName + " livePhoto:" + livePhotoFileName;
                        writeLog(logInfo, true);
                        if (score != null) {
                            if (score.doubleValue() >= matchInfo.getScoreIfSamePerson()) {
                                tpCount ++;
                            } else {
                                fnCount ++;
                            }
                        } else {
                            writeLog("[ERROR]score is error: " + score, true);
                        }
                        
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    writeLog(e1.getMessage(), true);
                } finally {
                    IOUtils.closeQuietly(idCardInput);
                    IOUtils.closeQuietly(livePhotoInput);
                }
                
            }
            
        } catch(IOException e){
            writeLog(e.getMessage(), true);
        } finally {
            stream.close();
        }
        
        matchInfo.setTpCount(tpCount);
        matchInfo.setFnCount(fnCount);
        
        matchInfo = this.negativesMatchTest(matchInfo);
        writeLog("" + matchInfo, true);
        
    }
    
    
    public MatchInfo negativesMatchTest(MatchInfo matchInfo) throws IOException {
        
        int fpCount = 0; // 负类判定为正类
        int tnCount = 0; // 负类判定为负类
        
        int negativesCount = 0; // 負類數
        //int errCount = 0;
        
        String dataPath = "D:\\users"; // users-male
        Path dataDir = Paths.get(dataPath);
        DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir);
        try {
            
            List<Path> pathList = Lists.newArrayList();
            for (Path e : stream) {
                pathList.add(e);
            }
            
            if (pathList.size() < 2) {
                writeLog("樣本數太小", true);
                return matchInfo;
            }
            
            List<String> userIdList = new ArrayList<String>();
            
            while (negativesCount < matchInfo.getNegativesCount()) {
                
                int index = new Random().nextInt(pathList.size());
                Path userPath1 = pathList.get(index);
                String userId1 = userPath1.toFile().getName();
                
                if (userIdList.contains(userId1)) {
                    continue;
                } else {
                    userIdList.add(userId1);
                }
                
                Path userPath2 = null;
                boolean findUser = false;
                do {
                    index = new Random().nextInt(pathList.size());
                    userPath2 = pathList.get(index);
                    String userId2 = userPath2.toFile().getName();
                    if (!userId1.equals(userId2)) {
                        findUser = true;
                    }
                } while (!findUser);
                
                String userId2 = userPath2.toFile().getName();
                
                // 獲取用戶1的身份證照片和用戶2的活體照片
                Path idCardPath = this.randomPath(userPath1, "Idcard-");
                Path livePhotoPath = this.randomPath(userPath2, "LivePhoto-");
                
                String idCardFileName = idCardPath.toFile().getName();
                String livePhotoFileName = livePhotoPath.toFile().getName();
                
                InputStream idCardInput = null;
                InputStream livePhotoInput = null;
                try {
                    idCardInput = new FileInputStream(idCardPath.toFile());
                    MultipartFile idCardMultipartFile = new MockMultipartFile(idCardFileName, idCardInput);
                    
                    livePhotoInput = new FileInputStream(livePhotoPath.toFile());
                    MultipartFile livePhotoMultipartFile = new MockMultipartFile(livePhotoFileName, livePhotoInput);
                    
                    // FaceTypeEnum.LIVE --> FaceTypeEnum.CERT
                    String result = aipService.match(FaceTypeEnum.CERT, idCardMultipartFile, FaceTypeEnum.CERT, livePhotoMultipartFile);
                    if(result.indexOf("SUCCESS") == -1) {
                        // error
                        writeLog("userId1:" + userId1 + "userId2:" + userId2 + " " + result, true);
                        //errCount ++;
                    } else {
                        //writeLog(result, true);
                        //idCardCount++;
                        
                        negativesCount ++;
                        
                        Map<String, Object> responseMap = JsonUtils.toMap(result);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> resultInResponseMap = (Map<String, Object>)responseMap.get("result");

                        Double score = this.getScoreFromMap(resultInResponseMap);
                        
                        String logInfo = "userId1:" + userId1 + " userId2:" + userId2 + " score:" + score + " idcard:" + idCardFileName + " livePhoto:" + livePhotoFileName;
                        writeLog(logInfo, true);
                        if (score != null) {
                            if (score.doubleValue() >= matchInfo.getScoreIfSamePerson()) {
                                fpCount ++;
                            } else {
                                tnCount ++;
                            }
                        } else {
                            writeLog("[ERROR]score is error: " + score, true);
                        }
                        
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    writeLog(e1.getMessage(), true);
                } finally {
                    IOUtils.closeQuietly(idCardInput);
                    IOUtils.closeQuietly(livePhotoInput);
                }
            
            }
            
        } catch(IOException e){
            writeLog(e.getMessage(), true);
        } finally {
            stream.close();
        }
        
//        String pnlogInfo = "positivesCount:" + positivesCount + " negativesCount:" + negativesCount + " errCount:" + errCount;
//        writeLog(pnlogInfo, true);
//        
//        String resultLogInfo = "tpCount:" + tpCount + " fpCount:" + fpCount + " fnCount:" + fnCount + " tnCount:" + tnCount;
//        writeLog(resultLogInfo, true);
        
        matchInfo.setFpCount(fpCount);
        matchInfo.setTnCount(tnCount);
        
        return matchInfo;
        
    }
    
    @Test
    public void identify() throws IOException {
        
        String dataPath = "D:\\users";
        Path dataDir = Paths.get(dataPath);
        DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir);
        
        int totalExampleCount = 30; // 樣本數
        int exampleCount = 0;
        int identifyCount = 0;
        int noIdentifyCount = 0;
        
        int identifyScore = 80;
        String filePrefix = "LivePhoto-"; // Idcard-/LivePhoto-
        
        try {
            
            List<Path> pathList = Lists.newArrayList();
            for (Path e : stream) {
                pathList.add(e);
            }
            
            if (pathList.size() < 2) {
                writeLog("樣本數太小", true);
                return;
            }
            
            while (exampleCount < totalExampleCount) {
                
                int index = new Random().nextInt(pathList.size());
                Path userPath = pathList.get(index);
                String userId = userPath.toFile().getName();
                
                // 獲取用戶的身份證照片或活體照片
                Path filePath = this.randomPath(userPath, filePrefix);
                String fileName = filePath.toFile().getName();
                
                InputStream input = null;
                try {
                    input = new FileInputStream(filePath.toFile());
                    MultipartFile idCardMultipartFile = new MockMultipartFile(fileName, input);
                    
                    String groupIdList = "xwallet_id_card,xwallet_live_photo";
                    String result = aipService.identify(groupIdList, null, null, idCardMultipartFile);
                    if(result.indexOf("SUCCESS") == -1) {
                        // error
                        writeLog("userId:" + userId + " " + result, true);
                        //errCount ++;
                    } else {
                        //writeLog(result, true);
                        exampleCount ++;
                        
                        Map<String, Object> responseMap = JsonUtils.toMap(result);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> resultInResponseMap = (Map<String, Object>)responseMap.get("result");
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> resultUserList = (List<Map<String, Object>>)resultInResponseMap.get("user_list");
                        if (CollectionUtils.isNotEmpty(resultUserList)) {
                            Map<String, Object> resultUserInfoMap = resultUserList.get(0);
                            Double score = this.getScoreFromMap(resultUserInfoMap);
                            
                            String logInfo = "userId:" + userId + " score:" + score + " source:" + fileName + " result:" + resultUserInfoMap;
                            writeLog(logInfo, true);
                            if (score != null) {
                                if (score.doubleValue() >= identifyScore) {
                                    identifyCount ++;
                                } else {
                                    noIdentifyCount ++;
                                }
                            } else {
                                writeLog("[ERROR]score is error: " + score, true);
                            }
                            
                        }
                        
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                    writeLog(e1.getMessage(), true);
                } finally {
                    IOUtils.closeQuietly(input);
                }
            
            }
            
        } catch(IOException e){
            writeLog(e.getMessage(), true);
        } finally {
            stream.close();
        }
        
        String resultLogInfo = "totalExampleCount:" + totalExampleCount + " identifyCount:" + identifyCount + " noIdentifyCount:" + noIdentifyCount;
        writeLog(resultLogInfo, true);
        
    }
    
    @Test
    public void negativesIdentifyTest() throws IOException {
        
        String dataPath = "D:\\negatives_face";
        Path dataDir = Paths.get(dataPath);
        
        int identifyCount = 0;
        int noIdentifyCount = 0;
        
        int identifyScore = 80;
            
        List<Path> files = Files.list(dataDir).collect(Collectors.toList());
        for (Path file : files) {
            
            String fileName = file.toFile().getName();
            
            InputStream input = null;
            try {
                input = new FileInputStream(file.toFile());
                MultipartFile idCardMultipartFile = new MockMultipartFile(fileName, input);
                
                String groupIdList = "xwallet_id_card,xwallet_live_photo";
                String result = aipService.identify(groupIdList, null, null, idCardMultipartFile);
                if(result.indexOf("SUCCESS") == -1) {
                    // error
                    writeLog("fileName:" + fileName + " " + result, true);
                    //errCount ++;
                } else {
                    //writeLog(result, true);
                    
                    Map<String, Object> responseMap = JsonUtils.toMap(result);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> resultInResponseMap = (Map<String, Object>)responseMap.get("result");
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> resultUserList = (List<Map<String, Object>>)resultInResponseMap.get("user_list");
                    if (CollectionUtils.isNotEmpty(resultUserList)) {
                        Map<String, Object> resultUserInfoMap = resultUserList.get(0);
                        Double score = this.getScoreFromMap(resultUserInfoMap);
                        
                        String logInfo = " score:" + score + " source:" + fileName + " result:" + resultUserInfoMap;
                        writeLog(logInfo, true);
                        if (score != null) {
                            if (score.doubleValue() >= identifyScore) {
                                identifyCount ++;
                            } else {
                                noIdentifyCount ++;
                            }
                        } else {
                            writeLog("[ERROR]score is error: " + score, true);
                        }
                        
                    }
                    
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                writeLog(e1.getMessage(), true);
            } finally {
                IOUtils.closeQuietly(input);
            }
        
        }
        
        String resultLogInfo = " identifyCount:" + identifyCount + " noIdentifyCount:" + noIdentifyCount;
        writeLog(resultLogInfo, true);
        
    }
    
    private Path randomPath(Path userIdPath, String fileNamePrefix) throws IOException {
        
        List<Path> pathList = Lists.newArrayList();
        List<Path> files = Files.list(userIdPath).collect(Collectors.toList());
        for(Path file : files) {
            String fileName = file.toFile().getName();
            if (StringUtils.startsWith(fileName, fileNamePrefix)) {
                pathList.add(file);
            }
        }
        
        int index = new Random().nextInt(pathList.size());
        Path path = pathList.get(index);
        
        return path;
    }
    
    private Double getScoreFromMap(Map<String, Object> map) {
        Object scoreObj = map.get("score");
        
        Double score = null;
        if (scoreObj instanceof Double) {
            score = (Double)scoreObj;
        } else if (scoreObj instanceof Integer) {
            Integer scoreInteger = (Integer)scoreObj;
            if (scoreInteger != null) {
                score = scoreInteger.doubleValue();
            }
        } else {
            writeLog("[ERROR]score type is unknown: " + scoreObj, true);
        }
        return score;
    }
    
}