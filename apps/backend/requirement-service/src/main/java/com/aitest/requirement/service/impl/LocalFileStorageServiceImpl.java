package com.aitest.requirement.service.impl;

import com.aitest.requirement.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 本地文件存储服务实现类
 */
@Slf4j
@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    @Value("${file.storage.local.path:./uploads}")
    private String uploadPath;

    /**
     * 上传文件
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 创建上传目录
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 生成文件名
        String originalFilename = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String newFilename = timestamp + "_" + uuid + "." + extension;

        // 保存文件
        Path filePath = uploadDir.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("文件上传成功: {}", newFilename);
        return newFilename;
    }

    /**
     * 下载文件
     */
    @Override
    public Resource downloadFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadPath).resolve(filename);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("文件不存在或无法读取: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("文件路径无效: " + filename, e);
        }
    }

    /**
     * 删除文件
     */
    @Override
    public boolean deleteFile(String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("删除文件失败: {}", filename, e);
            return false;
        }
    }
}
