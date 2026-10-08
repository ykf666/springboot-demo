package com.springboot.demo.controller;

import com.springboot.demo.annotation.CommonResp;
import com.springboot.demo.entity.FileServer;
import com.springboot.demo.exception.ServiceException;
import com.springboot.demo.repository.FileServerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 文件服务控制器
 * 提供文件上传和下载接口，均使用POST方法
 */
@Slf4j
@RestController
@RequestMapping("/file")
@CommonResp
public class FileServerController {

    @Value("${file.storage.path:./uploads}")
    private String storagePath;

    private Path storageLocation;

    @Autowired
    private FileServerRepository fileServerRepository;

    @PostConstruct
    public void init() {
        this.storageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("无法创建文件存储目录: " + storagePath, e);
        }
    }

    /**
     * 文件上传接口（POST）
     */
    @PostMapping("/upload")
    public FileServer upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new ServiceException(10, "上传文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (StringUtils.isNotBlank(originalFileName) && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + extension;

        Path targetLocation = this.storageLocation.resolve(fileName);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new ServiceException(10, "文件上传失败:" + e.getMessage());
        }

        FileServer fileServer = new FileServer();
        fileServer.setFileName(fileName);
        fileServer.setOriginalFileName(originalFileName);
        fileServer.setFileSize(file.getSize());
        fileServer.setFilePath(targetLocation.toAbsolutePath().toString());
        fileServerRepository.save(fileServer);

        log.info("文件上传成功: id={}, fileName={}", fileServer.getId(), fileName);
        return fileServer;
    }

    /**
     * 文件下载接口（POST）
     * 返回HttpEntity<StreamingResponseBody>类型
     */
    @PostMapping("/download/{fileId}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long fileId) {
        if (fileId == null) {
            return ResponseEntity.badRequest().build();
        }

        FileServer fileServer = fileServerRepository.findById(fileId).orElse(null);
        if (fileServer == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(fileServer.getFilePath());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        StreamingResponseBody stream = outputStream -> {
            Files.copy(filePath, outputStream);
        };

        String originalFileName = StringUtils.isNotBlank(fileServer.getOriginalFileName())
                ? fileServer.getOriginalFileName()
                : fileServer.getFileName();

        // 根据原始文件名的后缀自动推断 content-type，未知类型兜底为二进制流
        MediaType contentType = MediaTypeFactory.getMediaType(originalFileName)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        // 按 RFC 5987 编码，兼容中文等非 ASCII 文件名
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(originalFileName, StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentLength(fileServer.getFileSize())
                .contentType(contentType)
                .body(stream);
    }
}
