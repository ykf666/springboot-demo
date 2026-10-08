package com.springboot.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 文件服务表
 */
@Data
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Table(name = "file_server")
public class FileServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名称
     */
    @Column(name = "file_name", nullable = false)
    private String fileName;

    /**
     * 文件大小（字节）
     */
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * 文件存储路径
     */
    @Column(name = "file_path", nullable = false)
    private String filePath;

    /**
     * 文件原始名称（上传时的文件名）
     */
    @Column(name = "original_file_name")
    private String originalFileName;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
