package com.example.Controller;

import com.example.Entity.RestBeanNew;
import com.example.Service.QRCodeService;
import com.google.zxing.WriterException;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;

@Validated
@RestController
@RequestMapping("/api/QRCode")
@Slf4j
public class QRCodeController {
    @Resource
    QRCodeService qrCodeService;

    @GetMapping("/generateQRCode")
    public RestBeanNew<byte[]> generateQRCode() throws IOException, WriterException {
        byte[] qrCode = qrCodeService.generateQRCodeImage("https://example.com", 200, 200);
        return RestBeanNew.success(qrCode,"生成二维码");
//        return RestBeanNew.failure(400,"二维码生成失败");
    }


}
