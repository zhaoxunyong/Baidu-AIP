package com.zerofinance.aip.service;

import java.io.IOException;
import com.zerofinance.aip.enums.FaceTypeEnum;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Baidu AIP Api", description = "Baidu AIP Api", protocols = "http", produces = "application/json")
public interface AipService {

    /**
     * 创建用户组 
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="创建用户组", notes="用于创建一个空的用户组，如果用户组已存在 则返回错误；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query")
    })
    String addGroup(String groupId) throws IOException;

    /**
     * 删除用户组 <p>
     * 删除用户组下所有的用户信息及人脸信息 如果组不存在 则返回错误.
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="删除用户组", notes="删除用户组下所有的用户信息及人脸信息 如果组不存在 则返回错误；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query")
    })
    String delGroup(String groupId) throws IOException;

    /**
     * 组列表 <p>
     * 获取人脸库中用户组的列表.
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="组列表", notes="获取人脸库中用户组的列表；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    String listGroup() throws IOException;

    /**
     * 用户列表 <p>
     * 获取用户组中的用户列表 
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="用户列表", notes="获取用户组中的用户列表；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "start", defaultValue = "0", value = "start, 默认值0，起始序号", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "length", defaultValue = "100", value = "length, 返回数量，默认值100，最大值1000", required = true, dataType = "string", paramType = "query")
    })
    String listUser(String groupId, Integer start, Integer length) throws IOException;

    /**
     * 获取用户信息 <p>
     * 获取人脸库中某个用户的信息(user_info信息和用户所属的组)
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="获取用户信息", notes="获取人脸库中某个用户的信息(user_info信息和用户所属的组)；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "string", paramType = "query"),
    })
    String getUser(String groupId, String userId) throws IOException;

    /**
     * 人脸注册 <p>
     * 向人脸库中添加人脸(如果group,uid不存在, 则会自动创建用户组和注册用户) 一个用户组中只能有一个唯一的face_token
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="人脸注册", notes="用于从人脸库中新增用户，可以设定多个用户所在组，及组内用户的人脸图片；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "userId，不存在时会自动创建", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "desc", value = "图片的描述", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "file", value = "人脸图片", required = true, dataType = "__file", paramType = "form")
    })
    String addFace(String groupId, String userId, String desc, MultipartFile file) throws IOException;

    /**
     * 人脸更新 <p>
     * 用于对人脸库中指定用户，更新其下的人脸图像。 说明：针对一个user_id执行更新操作，新上传的人脸图像将覆盖该group_id中user_id的原有所有图像。
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
//    @ApiOperation(value="人脸更新", notes="用于对人脸库中指定用户，更新其下的人脸图像。 说明：针对一个user_id执行更新操作，新上传的人脸图像将覆盖该group_id中user_id的原有所有图像。；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
//    @ApiImplicitParams({
//        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
//        @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "string", paramType = "query"),
//        @ApiImplicitParam(name = "desc", value = "图片的描述", required = false, dataType = "string", paramType = "query"),
//        @ApiImplicitParam(name = "file", value = "人脸图片", required = true, dataType = "__file", paramType = "form")
//    })
//    String updateFace(String groupId, String userId, String desc, MultipartFile file) throws IOException;
//    

    /**
     * 删除人脸 <p>
     * 删除用户下的某一张人脸 如果该用户下没有其他人脸了则同时删除用户
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="删除人脸", notes="删除用户下的某一张人脸 如果该用户下没有其他人脸了则同时删除用户；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "faceToken", value = "人脸id", required = false, dataType = "string", paramType = "query"),
    })
    String delFace(String groupId, String userId, String faceToken) throws IOException;
        

    /**
     * 人脸列表 <p>
     * 获取一个用户下的人脸列表
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="人脸列表", notes="获取一个用户下的人脸列表；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "groupId", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "userId", required = true, dataType = "string", paramType = "query"),
    })
    String listFace(String groupId, String userId) throws IOException;

    /**
     * 人脸检测 
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="人脸检测", notes="人脸检测：检测图片中的人脸并标记出位置信息；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "faceTypeEnum", value = "人脸的类型，不选择默认为LIVE：<br/>LIVE表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等；<br/>IDCARD表示身份证芯片照：二代身份证内置芯片中的人像照片；<br/>WATERMARK表示带水印证件照：一般为带水印的小图，如公安网小图；<br/>CERT表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片。", required = false, dataType = "enum", paramType = "query"),
        @ApiImplicitParam(name = "file", value = "人脸图片", required = true, dataType = "__file", paramType = "form")
    })
    String detect(FaceTypeEnum faceTypeEnum, /* @ApiParam(value="人脸检测",required = true) */ MultipartFile file) throws IOException;

    /**
     * 人脸认证
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="人脸认证", notes="1：N人脸搜索：也称为1：N识别，在指定人脸集合中，找到最相似的人脸；<br/>1：N人脸认证：基于uid维度的1：N识别，由于uid已经锁定固定数量的人脸，所以检索范围更聚焦；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupIdList", value = "groupId，多个时用逗号分隔", required = true, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "userId", value = "userId，为空时搜索该group下的所有用户", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "maxUserNum", value = "返回探索后的最大用户数量，为空时默认为1", required = false, dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "file", value = "人脸图片", required = true, dataType = "__file", paramType = "form")
    })
    String identify(String groupIdList, String userId, Integer maxUserNum, MultipartFile file) throws IOException;

    /**
     * 人脸对比 
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="人脸对比", notes="两张人脸图片相似度对比：比对两张图片中人脸的相似度，并返回相似度分值；<br/>多种图片类型：支持生活照、证件照、身份证芯片照、带网纹照四种类型的人脸对比；<br/>活体检测：基于图片中的破绽分析，判断其中的人脸是否为二次翻拍（举例：如用户A用手机拍摄了一张包含人脸的图片一，用户B翻拍了图片一得到了图片二，并用图片二伪造成用户A去进行识别操作，这种情况普遍发生在金融开户、实名认证等环节。）；<br/>质量检测：返回模糊、光照等质量检测信息，用于辅助判断图片是否符合识别要求；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "faceTypeEnum1", value = "人脸的类型，不选择默认为LIVE：<br/>LIVE表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等；<br/>IDCARD表示身份证芯片照：二代身份证内置芯片中的人像照片；<br/>WATERMARK表示带水印证件照：一般为带水印的小图，如公安网小图；<br/>CERT表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片。", required = false, dataType = "enum", paramType = "query"),
        @ApiImplicitParam(name = "file1", value = "人脸图片1", required = true, dataType = "__file", paramType = "form"),
        @ApiImplicitParam(name = "faceTypeEnum2", value = "人脸的类型，不选择默认为LIVE：<br/>LIVE表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等；<br/>IDCARD表示身份证芯片照：二代身份证内置芯片中的人像照片；<br/>WATERMARK表示带水印证件照：一般为带水印的小图，如公安网小图；<br/>CERT表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片。", required = false, dataType = "enum", paramType = "query"),
        @ApiImplicitParam(name = "file2", value = "人脸图片2", required = true, dataType = "__file", paramType = "form")
    })
    String match(FaceTypeEnum faceTypeEnum1, MultipartFile file1, FaceTypeEnum faceTypeEnum2, MultipartFile file2) throws IOException;

    /**
     * 活体检测
     * http://ai.baidu.com/docs#/Face_Private_API/a532be61
     * 
     * @throws IOException
     */
    @ApiOperation(value="活体检测", notes="人脸基础信息，人脸质量检测，基于图片的活体检测；<br/>单张图片大小不能超過10M；<br/>请参考<a href='http://ai.baidu.com/docs#/Face_Private_API/a532be61' target='_blank'>http://ai.baidu.com/docs#/Face_Private_API/a532be61</a>")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "faceTypeEnum", value = "人脸的类型，不选择默认为LIVE：<br/>LIVE表示生活照：通常为手机、相机拍摄的人像图片、或从网络获取的人像图片等；<br/>IDCARD表示身份证芯片照：二代身份证内置芯片中的人像照片；<br/>WATERMARK表示带水印证件照：一般为带水印的小图，如公安网小图；<br/>CERT表示证件照片：如拍摄的身份证、工卡、护照、学生证等证件图片。", required = false, dataType = "enum", paramType = "query"),
        @ApiImplicitParam(name = "file", value = "人脸图片", required = true, dataType = "__file", paramType = "form")
    })
    String liveness(FaceTypeEnum faceTypeEnum, MultipartFile file) throws IOException;
}