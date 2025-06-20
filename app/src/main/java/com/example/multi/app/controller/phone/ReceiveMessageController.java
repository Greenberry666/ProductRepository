package com.example.multi.app.controller.phone;

import com.example.multi.module.textMessage.service.TextMessageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReceiveMessageController {

    @Autowired
    private TextMessageService messageService;


    // 同步发送
    @SneakyThrows
    @GetMapping("/receive")
    public String sendSms(@RequestParam(name = "phone") String phone) {
        return messageService.sendSms(phone);
    }

    // 多线程发送
    @SneakyThrows
    @PostMapping("/receive/batch")
    public String sendSmsBatch(@RequestBody List<String> phones) {
        return messageService.sendSmsBatch(phones);
    }

    // 异步发送
    @SneakyThrows
    @GetMapping("/receive/async")
    public String sendSmsAsync(@RequestParam(name = "phone") String phone) {
        return messageService.sendSmsAsync(phone);
    }

}
