package com.example.multi.app;

import com.alibaba.druid.filter.config.ConfigTools;

import java.util.Base64;

public class DruidEncryptorUtils {
    public static void main(String[] args) {
        try {
            String password = "123456";
            String[] keyPair = ConfigTools.genKeyPair(512);
            String privateKey = keyPair[0];
            String publicKey = keyPair[1];
            String encryptedPassword = ConfigTools.encrypt(privateKey, password);

            System.out.println("privateKey: " + privateKey);
            System.out.println("publicKey: " + publicKey);
            System.out.println("encryptedPassword: " + encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}