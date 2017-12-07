package com.kaishengit.file.impl;

import com.google.gson.Gson;
import com.kaishengit.file.FileStore;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author zhao
 */
@Component
public class QiNiuFileStore implements FileStore {

    @Value("${qiniu.ak}")
    private String accessKey;
    @Value("${qiniu.sk}")
    private String secretKey;
    @Value("${qiniu.domain}")
    private String domain;
    @Value("${qiniu.bucket}")
    private String bucketName;

    /**
     * 上传文件
     * @param inputStream
     * @return
     */
    @Override
    public String uploadFile(InputStream inputStream) throws IOException {
        Configuration cfg = new Configuration(Zone.zone1());
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucketName);

        Response response = uploadManager.put(IOUtils.toByteArray(inputStream), null, upToken);
        DefaultPutRet defaultPutRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

        return defaultPutRet.key;
    }

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    @Override
    public InputStream downloadFile(String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        String finalUrl = String.format("%s/%s",domain,encodedFileName);

        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(finalUrl).openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        return inputStream;
    }
}
