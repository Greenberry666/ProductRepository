package com.example.multi.app.domain;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class ImageScaleVO {
    private String imageURL;
    private double ar;

    public double iamgeAR() {
        String regex = "(\\d+)x(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(imageURL);

        if (matcher.find()) {
            double width = Integer.parseInt(matcher.group(1));
            double height = Integer.parseInt(matcher.group(2));
            ar = width / height;
        }
        return ar;
    }
}
