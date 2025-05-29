package com.example.multi.module.textMessage.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.multi.module.textMessage.entity.TextMessage;
import com.example.multi.module.textMessage.mapper.TextMessageMapper;
import com.example.multi.module.textMessageTask.entity.TextMessageTask;
import com.example.multi.module.textMessageTask.mapper.TextMessageTaskMapper;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TextMessageService {

//    @Value("${aliyun.sms.accessKeyId}")
//    private String accessKeyId;
//
//    @Value("${aliyun.sms.accessKeySecret}")
//    private String accessKeySecret;
//
//    @Value("${aliyun.sms.regionId}")
//    private String regionId;

//    @Value("${aliyun.sms.signName}")
//    private String signName;
//
//    @Value("${aliyun.sms.templateCode}")
//    private String templateCode;

    @Resource
    private TextMessageMapper textMessageMapper;
    @Resource
    private TextMessageTaskMapper textMessageTaskMapper;


    private static final String Id = "A";
    private static final String Secret = "B";
    private static final String REGION_ID = "cn-hangzhou";

    private final IAcsClient client;

    public TextMessageService() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, Id, Secret);
        this.client = new DefaultAcsClient(profile);
    }

    public String sendSms(String phone) {
        try {
            // 发送短信逻辑（调用阿里云SDK）
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName("签名名称");
            request.setTemplateCode("模板ID");
            request.setTemplateParam("{\"code\":\"12345\"}");
            SendSmsResponse response = client.getAcsResponse(request);

            // 记录发送情况
            TextMessage message = new TextMessage();
            message.setPhone(phone);
            message.setCode("12345");
            message.setStatus(response.getCode());
            message.setSendTime(BaseUtils.currentSeconds());
            message.setCreateTime(BaseUtils.currentSeconds());
            message.setUpdateTime(BaseUtils.currentSeconds());
            message.setIsDeleted(0);
            textMessageMapper.insert(message);

            return "发送成功";
        } catch (Exception e) {
            return "发送失败: " + e.getMessage();
        }
    }

    public String sendSmsBatch(List<String> phones) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 创建线程池
        for (String phone : phones) {
            executor.submit(() -> {
                sendSms(phone); // 调用同步发送方法
            });
        }
        executor.shutdown();
        return "多线程发送任务已启动";
    }

    public String sendSmsAsync(String phone) {

        return "短信发送任务已接收";
    }

    // 定时任务
    // 定时任务：扫描未发送的任务并发送短信
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void sendPendingMessages() {
        List<TextMessageTask> tasks = textMessageTaskMapper.findPendingTasks();
        for (TextMessageTask task : tasks) {
            try {
                // 发送短信
                SendSmsRequest request = new SendSmsRequest();
                request.setPhoneNumbers(task.getPhone());
                //request.setSignName(signName);
                //request.setTemplateCode(templateCode);
                request.setTemplateParam("{\"code\":\"" + task.getCode() + "\"}");
                SendSmsResponse response = client.getAcsResponse(request);

                // 更新任务状态
                task.setStatus(response.getCode());
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskMapper.update(task);

                // 记录发送情况
                TextMessage message = new TextMessage();
                message.setPhone(task.getPhone());
                message.setCode(task.getCode());
                message.setStatus(response.getCode());
                message.setCreateTime(BaseUtils.currentSeconds());
                message.setUpdateTime(BaseUtils.currentSeconds());
                textMessageMapper.insert(message);
            } catch (Exception e) {
                task.setStatus("FAILED");
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskMapper.update(task);
            }
        }
    }

}

