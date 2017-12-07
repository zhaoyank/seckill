package com.kaishengit.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhao
 */
public interface FileStore {

    /**
     * 上传文件
     * @param inputStream
     * @return
     */
    String uploadFile(InputStream inputStream) throws IOException;

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    InputStream downloadFile(String fileName) throws IOException;


}
