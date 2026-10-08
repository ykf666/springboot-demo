package com.springboot.demo.repository;

import com.springboot.demo.entity.FileServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文件服务Repository
 */
@Repository
public interface FileServerRepository extends JpaRepository<FileServer, Long> {
}
