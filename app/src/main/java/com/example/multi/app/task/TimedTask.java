package com.example.multi.app.task;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.example.multi.module.textMessage.entity.TextMessage;
import com.example.multi.module.textMessage.mapper.TextMessageMapper;
import com.example.multi.module.textMessage.service.TextMessageService;
import com.example.multi.module.textMessageTask.entity.TextMessageTask;
import com.example.multi.module.textMessageTask.mapper.TextMessageTaskMapper;
import com.example.multi.module.utils.BaseUtils;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class TimedTask {
    @Resource
    private TextMessageService textMessageService;
    @Resource
    private TextMessageTaskMapper textMessageTaskMapper;
    @Resource
    private TextMessageMapper textMessageMapper;

    // 定时任务：扫描未发送的任务并发送短信
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void sendPendingMessages() {
        List<TextMessageTask> tasks = textMessageTaskMapper.findPendingTasks();
        for (TextMessageTask task : tasks) {
            try {
                // 发送短信
                String responseCode = textMessageService.sendSms(task.getPhone());

                // 更新任务状态
                task.setStatus(responseCode);
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskMapper.update(task);

                // 记录发送情况
                TextMessage message = new TextMessage();
                message.setPhone(task.getPhone());
                message.setCode(task.getCode());
                message.setStatus(responseCode);
                message.setCreateTime(BaseUtils.currentSeconds());
                message.setUpdateTime(BaseUtils.currentSeconds());
                textMessageMapper.insert(message);
            } catch (Exception e) {
                task.setStatus("发送失败");
                task.setUpdateTime(BaseUtils.currentSeconds());
                textMessageTaskMapper.update(task);
            }
        }
    }
}
