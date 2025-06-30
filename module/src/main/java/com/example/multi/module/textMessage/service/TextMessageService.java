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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TextMessageService {
    private static final String Id = "A"; // 替换为你的 AccessKeyId
    private static final String Secret = "B"; // 替换为你的 AccessKeySecret
    private static final String REGION_ID = "cn-hangzhou"; // 替换为你的 RegionId

    private  final IAcsClient client;

    @Resource
    public TextMessageMapper textMessageMapper;
    @Resource
    public TextMessageTaskMapper textMessageTaskMapper;

    public TextMessage getById(BigInteger id) {
        return textMessageMapper.getById(id);
    }

    public TextMessage extractById(BigInteger id) {
        return textMessageMapper.extractById(id);
    }

    public int insert(TextMessage textMessage) {
        return textMessageMapper.insert(textMessage);
    }

    public int update(TextMessage textMessage) {
        return textMessageMapper.update(textMessage);
    }

    public int delete(BigInteger id) {
        return textMessageMapper.delete(id, BaseUtils.currentSeconds());
    }

    public TextMessageService() {
        IClientProfile profile = DefaultProfile.getProfile(REGION_ID, Id , Secret);
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

            TextMessage message = createTextMessage(phone, response.getCode());
            textMessageMapper.insert(message);

            return response.getCode();
        } catch (Exception e) {
            TextMessage message = createTextMessage(phone, "发送失败: " + e.getMessage());
            message.setStatus(2);
            textMessageMapper.insert(message);

            return "发送失败: " + e.getMessage();
        }
    }
    private TextMessage createTextMessage(String phone, String responseCode) {
        TextMessage message = new TextMessage();
        message.setPhone(phone);
        message.setCode("12345");
        message.setStatus(mapResponseCodeToStatus(responseCode));
        message.setSendTime(BaseUtils.currentSeconds());
        message.setCreateTime(BaseUtils.currentSeconds());
        message.setUpdateTime(BaseUtils.currentSeconds());
        message.setIsDeleted(0);
        return message;
    }


    public String sendSmsBatch(List<String> phones) {
        ExecutorService executor = Executors.newFixedThreadPool(10); // 创建线程池
        //线程池中最多可以同时运行 10 个线程。
        for (String phone : phones) {
            //executor.submit：ExecutorService 接口的一个方法，用于提交一个任务到线程池中执行。
            //() -> { sendSms(phone); }：这是一个 Lambda 表达式，表示一个实现了 Runnable 接口的匿名内部类。
            executor.submit(() -> {// 提交任务到线程池
                sendSms(phone); // 调用同步发送方法
            });
        }
        executor.shutdown();// 关闭线程池
        return "多线程发送任务已启动";
    }

    @Async
    public CompletableFuture<String> sendSmsAsync(String phone) {
        try {
            // 创建短信任务记录
            TextMessageTask task = new TextMessageTask();
            task.setPhone(phone);
            task.setCode("12345");
            task.setStatus(0); // 状态为 0 表示待发送
            task.setCreateTime(BaseUtils.currentSeconds());
            task.setUpdateTime(BaseUtils.currentSeconds());
            task.setIsDeleted(0);
            textMessageTaskMapper.insert(task);

            return CompletableFuture.completedFuture("发送成功");
        } catch (Exception e) {
            return CompletableFuture.completedFuture("发送失败: " + e.getMessage());
        }
    }


    private int mapResponseCodeToStatus(String responseCode) {
        if ("OK".equals(responseCode)) {
            return 1;
        } else {
            return 2;
        }
    }

    public List<TextMessage> getTextMessagesToExcel(){return textMessageMapper.getTextMessageToExcel();}


}
//executor.submit 方法
//executor.submit 是 ExecutorService 接口中的一个方法，用于提交一个任务到线程池中执行。这个方法接受一个 Callable 或 Runnable 类型的参数。
//Runnable：一个没有返回值的任务。
//Callable：一个有返回值的任务，通常返回 Future 对象。
//Lambda 表达式
//    () -> {
//    sendSms(phone); // 调用同步发送方法
//}



