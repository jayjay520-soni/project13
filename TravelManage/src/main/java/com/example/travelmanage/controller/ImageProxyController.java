package com.example.travelmanage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// 这里改成 jakarta.servlet
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class ImageProxyController {

    @GetMapping("/proxy-image")
    public void proxyImage(@RequestParam String url, HttpServletResponse response) throws IOException {
        // 验证 URL 必须是百度图片（可选，防止滥用）
        if (!url.startsWith("https://img0.baidu.com/") && !url.startsWith("https://img1.baidu.com/")
                && !url.startsWith("https://img2.baidu.com/") && !url.startsWith("https://img3.baidu.com/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "仅支持百度图片链接");
            return;
        }

        // 发起请求到百度图片，并添加 Referer 绕过防盗链
        URL imgUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
        conn.setRequestMethod("GET");
        // 关键：模拟浏览器 Referer，绕过百度防盗链
        conn.setRequestProperty("Referer", "https://image.baidu.com/");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        // 将百度返回的图片流转发给前端
        try (InputStream is = conn.getInputStream();
             OutputStream os = response.getOutputStream()) {

            // 设置响应类型为图片
            String contentType = conn.getContentType();
            response.setContentType(contentType != null ? contentType : "image/jpeg");

            // 流式传输
            is.transferTo(os);
            os.flush();
        } finally {
            conn.disconnect();
        }
    }
}