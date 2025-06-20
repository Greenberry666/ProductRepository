package com.example.multi.module.utils;

public class SmsUtils {
//    public static void sendMessage(String signName, String templateCode, String phoneNumbers, String code) {
//        IAcsClient client = createClient();
//        SendSmsRequest sendSmsRequest = new SendSmsRequest()
//                .setPhoneNumbers(phoneNumbers)
//                .setSignName(signName)
//                .setTemplateCode(templateCode)
//                .setTemplateParam("{\"code\":\"" + code + "\"}");
//        try {
//            SendSmsResponse sendSmsResponse = client.getAcsResponse(sendSmsRequest);
//            System.out.println(sendSmsResponse.getBody());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static IAcsClient createClient() {
//        String accessKeyId = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
//        String accessKeySecret = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
//        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//        return new DefaultAcsClient(profile);
//    }
}