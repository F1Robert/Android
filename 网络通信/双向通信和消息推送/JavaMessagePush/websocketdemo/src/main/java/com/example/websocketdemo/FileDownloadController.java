package com.example.websocketdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/download")
public class FileDownloadController {

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/app-debug.apk")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        // 获取资源文件的URL
        Resource fileResource = resourceLoader.getResource("classpath:app/app-debug.apk");

        if (fileResource.exists() && fileResource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"app-debug.apk\"")
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
