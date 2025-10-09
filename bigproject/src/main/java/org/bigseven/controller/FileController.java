package org.bigseven.controller;

import lombok.RequiredArgsConstructor;
import org.bigseven.constant.ExceptionEnum;
import org.bigseven.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件处理控制器
 *
 * @author v185v
 * &#064;date 2025/9/24
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 通过文件名获取图片资源并返回HTTP响应
     * @param filename 图片文件名
     * @return 包含图片资源的ResponseEntity对象，如果文件不存在或不可读则返回404
     */
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.RESOURCE_NOT_FOUND, e);
        }
    }
}
