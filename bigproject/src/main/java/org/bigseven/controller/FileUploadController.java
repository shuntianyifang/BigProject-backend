package org.bigseven.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigseven.result.AjaxResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author v185v
 * &#064;date 2025/9/18
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    @Value("${file.upload-dir:uploads/}")
    private String uploadPath;

    @Value("${feedback.image-base-url:/api/file/uploads/}")
    private String imageBaseUrl;

    /**
     * 文件上传接口,还没做统一错误处理
     * @param files 要上传的文件数组
     * @return 包含上传文件访问URL列表的Ajax结果对象
     */
    @PostMapping("/uploads")
    public AjaxResult<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();

        /// 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            /// 创建文件上传目录
            uploadDir.mkdirs();
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            try {
                //// 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID() + fileExtension;

                /// 保存文件
                Path filePath = Paths.get(uploadPath, uniqueFilename);
                Files.write(filePath, file.getBytes());

                /// 使用统一的URL格式
                fileUrls.add(uniqueFilename);

            } catch (IOException e) {
                log.error("文件上传失败: ", e);
                return AjaxResult.fail(500, "文件上传失败");
            }
        }

        return AjaxResult.success(fileUrls);
    }
}
