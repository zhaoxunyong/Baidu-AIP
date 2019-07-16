package com.zerofinance.aip.service.impl;

import java.io.IOException;
import java.io.InputStream;

import com.aeasycredit.commons.json.JsonUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.zerofinance.aip.config.AipProperties;
import com.zerofinance.aip.enums.FaceTypeEnum;
import com.zerofinance.aip.model.MatchParams;
import com.zerofinance.aip.service.AipService;
import com.zerofinance.aip.utils.ImageUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * PrivateAipServiceImpl
 * 
 * @see http://ai.baidu.com/docs#/PrivateAI_Introduction/top
 * @see http://ai.baidu.com/docs#/Face_Private_API/top
 */
@RestController
@RequestMapping("/api")
public class PrivateAipServiceImpl implements AipService {

    // private static final String PREFIX_URL = "http://192.168.67.240:8300";
    // private static final String APPID = "xwallet";
    private final static String IMAGE_TYPE = "BASE64";
    private final static String URL_PREFIX = "/face-api/v3";

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private AipProperties aipProperties;

    @Override
    @PostMapping("/addGroup")
    public String addGroup(String groupId) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        return this.process(URL_PREFIX+"/group/add", paramMap);
    }

    @Override
    @PostMapping("/delGroup")
    public String delGroup(String groupId) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        return this.process(URL_PREFIX+"/group/delete", paramMap);
    }

    @Override
    @PostMapping("/listGroup")
    public String listGroup() throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("start", "0");
        paramMap.add("length", "1000");
        return this.process(URL_PREFIX+"/group/list", paramMap);
    }

    @Override
    @PostMapping("/listUser")
    public String listUser(String groupId, Integer start, Integer length) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        if(start != null) {
            paramMap.add("start", String.valueOf(start));
        }
        if(length != null) {
            paramMap.add("length", String.valueOf(length));
        }
            
        return this.process(URL_PREFIX+"/user/list", paramMap);
    }

    @Override
    @PostMapping("/getUser")
    public String getUser(String groupId, String userId) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        paramMap.add("user_id", userId);
        
        return this.process(URL_PREFIX+"/user/get", paramMap);
    }

    @Override
    @PostMapping("/addFace")
    public String addFace(String groupId, String userId, String desc, MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        try {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("group_id", groupId);
            paramMap.add("user_id", userId);
            if (StringUtils.isNotBlank(desc)) {
                paramMap.add("user_info", desc);
            }
            paramMap.add("image_type", IMAGE_TYPE);
            
            String image = ImageUtils.encodeImgageToBase64(input);
            paramMap.add("image", image);
            return this.process(URL_PREFIX+"/face/add", paramMap);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

//    @Override
//    @PostMapping("/updateFace")
//    public String updateFace(String groupId, String userId, String desc, MultipartFile file) throws IOException {
//        InputStream input = file.getInputStream();
//        try {
//            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
//            paramMap.add("group_id", groupId);
//            paramMap.add("user_id", userId);
//            if (StringUtils.isNotBlank(desc)) {
//                paramMap.add("user_info", desc);
//            }
//            paramMap.add("image_type", IMAGE_TYPE);
//            
//            String image = ImageUtils.encodeImgageToBase64(input);
//            paramMap.add("image", image);
//            return this.process("/face-api/v3/face/update", paramMap);
//        } finally {
//            IOUtils.closeQuietly(input);
//        }
//    }

    @Override
    @PostMapping("/delFace")
    public String delFace(String groupId, String userId, String faceToken) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        paramMap.add("user_id", userId);
        paramMap.add("face_token", faceToken);
        
        return this.process(URL_PREFIX+"/face/delete", paramMap);
    }


    @Override
    @PostMapping("/listFace")
    public String listFace(String groupId, String userId) throws IOException {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        paramMap.add("user_id", userId);
        
        return this.process(URL_PREFIX+"/face/list", paramMap);
    }

    @Override
    @PostMapping("/detect")
    public String detect(FaceTypeEnum faceTypeEnum, MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        try {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("image_type", IMAGE_TYPE);
            if(faceTypeEnum != null) {
                paramMap.add("face_type", faceTypeEnum.name());
            } else {
                paramMap.add("face_type", "LIVE");
            }
            paramMap.add("max_face_num", 10);
            paramMap.add("face_field", "age,beauty,expression,face_shape,gender,glasses,race,quality,face_type,parsing,feature");
            
            String image = ImageUtils.encodeImgageToBase64(input);
            paramMap.add("image", image);
            return this.process(URL_PREFIX+"/face/detect", paramMap);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    @PostMapping("/identify")
    public String identify(String groupIdList, String userId, Integer maxUserNum, MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        try {
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("group_id_list", groupIdList);
            paramMap.add("image_type", IMAGE_TYPE);
            if(StringUtils.isNotBlank(userId)) {
                paramMap.add("user_id", userId);
            }
            if(maxUserNum != null && maxUserNum.intValue() > 0) {
                paramMap.add("max_user_num", String.valueOf(maxUserNum));
            }
            String image = ImageUtils.encodeImgageToBase64(input);
            paramMap.add("image", image);
            return this.process(URL_PREFIX+"/face/identify", paramMap);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    @Override
    @PostMapping("/match")
    public String match(FaceTypeEnum faceTypeEnum1, MultipartFile file1, FaceTypeEnum faceTypeEnum2, MultipartFile file2) throws IOException {
        InputStream input1 = file1.getInputStream();
        InputStream input2 = file2.getInputStream();
        try {
            String faceType1 = "LIVE";
            String faceType2 = "LIVE";
            if(faceTypeEnum1 != null) {
                faceType1 = faceTypeEnum1.name();
            }
            if(faceTypeEnum2 != null) {
                faceType2 = faceTypeEnum2.name();
            }
            
            String image1 = ImageUtils.encodeImgageToBase64(input1);
            MatchParams param1 = new MatchParams(image1, IMAGE_TYPE, faceType1);

            String image2 = ImageUtils.encodeImgageToBase64(input2);
            MatchParams param2 = new MatchParams(image2, IMAGE_TYPE, faceType2);
            
            return this.process4Body(URL_PREFIX+"/face/match", Lists.newArrayList(param1, param2));
        } finally {
            IOUtils.closeQuietly(input1);
            IOUtils.closeQuietly(input2);
        }
    }

	@Override
    @PostMapping("/liveness")
	public String liveness(FaceTypeEnum faceTypeEnum, MultipartFile file) throws IOException {
        InputStream input = file.getInputStream();
        try {
            String faceType = "LIVE";
            if(faceTypeEnum != null) {
                faceType = faceTypeEnum.name();
            }
            
            String image = ImageUtils.encodeImgageToBase64(input);
            MatchParams param = new MatchParams(image, IMAGE_TYPE, faceType);
            
            return this.process4Body(URL_PREFIX+"/face/liveness", Lists.newArrayList(param));
        } finally {
            IOUtils.closeQuietly(input);
        }
	}

    private String builderUrl(String url) {
        String serverUrl = aipProperties.getServerUrl();
        String appId = aipProperties.getAppId();
        return new StringBuilder(serverUrl).append(url).append("?appid=").append(appId).toString();
    }

    private String process(String path, MultiValueMap<String, Object> paramMap) throws IOException {
        String url = builderUrl(path);
        // curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"group_id":"xwallet"}' http://192.168.108.191:8300/face-api/v3/group/add?appid=xxx

        HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);// 不能添加这行，否则报format error
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap, headers);
        ResponseEntity<String> result = restTemplate.postForEntity(url, requestEntity, String.class);

        // if(result.getStatusCodeValue() != 200) {
        //     throw new IOException("Internal Server Error");
        // }
        
        return jsonFormatter(result.getBody());

        /* 
        // 方式二
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("group_id", groupId);
        String content = restTemplate.postForObject(url, paramMap, String.class); */

        /* 
        // 方式三
        url += "&group_id="+groupId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>("", headers);
        ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class); */

        /* 
        // 方式四
        String body = "{"+
            "\"group_id\": \""+groupId+"\""+ 
        "}";
        // 设置HTTP请求头信息，实现编码等  
        HttpHeaders requestHeaders = new HttpHeaders(); 
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        // requestHeaders.add("appVersion", APP_VERSION);  
        HttpEntity<String> entity = new HttpEntity<String>(body, requestHeaders);
        ResponseEntity<String> result = restTemplate.exchange(  
                url,  
                HttpMethod.POST,  
                entity,  
                String.class);  */
    }

    private String process4Body(String path, Object obj) throws IOException {
        String url = builderUrl(path);

        String body = JsonUtils.toJson(obj);
        HttpHeaders requestHeaders = new HttpHeaders(); 
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(body, requestHeaders);
        ResponseEntity<String> result = restTemplate.exchange(  
                url,  
                HttpMethod.POST,  
                entity,  
                String.class);

        // if(result.getStatusCodeValue() != 200) {
        //     throw new IOException("Internal Server Error");
        // }
        
        return jsonFormatter(result.getBody());
    }

    private String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    
}