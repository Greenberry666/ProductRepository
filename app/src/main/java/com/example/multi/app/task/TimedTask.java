package com.example.multi.app.task;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.example.multi.module.textMessage.entity.TextMessage;
import com.example.multi.module.textMessage.mapper.TextMessageMapper;
import com.example.multi.module.textMessage.service.TextMessageService;
import com.example.multi.module.textMessageTask.entity.TextMessageTask;
import com.example.multi.module.textMessageTask.mapper.TextMessageTaskMapper;
import com.example.multi.module.textMessageTask.service.TextMessageTaskService;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class TimedTask {
    @Resource
    private TextMessageService textMessageService;
    @Resource
    private TextMessageTaskService textMessageTaskService;

    // 定时任务：扫描未发送的任务并发送短信
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void sendPendingMessages() {
        List<TextMessageTask> tasks = textMessageTaskService.findPendingTasks();
        for (TextMessageTask task : tasks) {
            try {
                // 发送短信
                String responseCode = textMessageService.sendSms(task.getPhone());


                // 更新任务状态
                int status;
                if ("OK".equals(responseCode)) {
                    status = 1;
                } else {
                    status = 2;
                }
                task.setStatus(status);
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskService.update(task);

                // 记录发送情况
                TextMessage message = new TextMessage();
                message.setPhone(task.getPhone());
                message.setCode(task.getCode());
                message.setStatus(status);
                message.setCreateTime(BaseUtils.currentSeconds());
                message.setUpdateTime(BaseUtils.currentSeconds());
                textMessageService.insert(message);
            } catch (Exception e) {
                task.setStatus(2);
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskService.update(task);
            }
        }
    }
}
