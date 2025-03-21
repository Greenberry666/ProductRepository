package com.example.multi.app.domain;

import lombok.Data;


@Data
public class ImageScaleVO {
    private String imageURL;
    private double ar;

    public void setImageURL(String imageURL) {
        if (imageURL == null) {
            this.imageURL = "0";
        } else {
            this.imageURL = imageURL;
        }
    }

    public void setAr(double ar) {
        if (ar == 0) {
            this.ar = 0.0;
        } else {
            this.ar = ar;
        }
    }

}
