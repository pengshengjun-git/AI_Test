package com.aitest.requirement.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 下载文件
     */
    Resource downloadFile(String filename) throws IOException;

    /**
     * 删除文件
     */
    boolean deleteFile(String filename);
}
