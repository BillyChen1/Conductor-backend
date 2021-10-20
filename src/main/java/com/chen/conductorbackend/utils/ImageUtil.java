package com.chen.conductorbackend.utils;

import com.aliyun.facebody20191230.Client;
import com.aliyun.facebody20191230.models.CompareFaceRequest;
import com.aliyun.facebody20191230.models.CompareFaceResponseBody;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Component
public class ImageUtil {

    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Value("${aliyun.facebodyEndpoint}")
    private String facebodyEndpoint;

    public String uploadImage(InputStream inputStream, String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //获取文件类型
        String contentType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        // 创建PutObjectRequest对象。
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);

        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        metadata.setContentType("image/" + contentType);
        putObjectRequest.setMetadata(metadata);

        // 上传。
        ossClient.putObject(putObjectRequest);

        //返回临时的签名url
        // 设置URL过期时间为1年。
        Date expiration = new Date(System.currentTimeMillis()+ 3600 * 1000 * 24 * 365);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);

        // 关闭OSSClient。
        ossClient.shutdown();

        return url.toString();
    }

    public CompareFaceResponseBody.CompareFaceResponseBodyData getImageCompareResult(String urlA, String urlB) throws Exception {

        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
//        config.regionId = "cn-shanghai";
        // 访问的域名
        config.endpoint = facebodyEndpoint;
        Client facebodyClient = new Client(config);

        CompareFaceRequest compareFaceRequest = new CompareFaceRequest()
                .setImageURLA(urlA)
                .setImageURLB(urlB);
        // 复制代码运行请自行打印 API 的返回值
//        CompareFaceResponseBody.CompareFaceResponseBodyData data = facebodyClient.compareFace(compareFaceRequest).getBody().getData();
        return facebodyClient.compareFace(compareFaceRequest).getBody().getData();

    }

}
