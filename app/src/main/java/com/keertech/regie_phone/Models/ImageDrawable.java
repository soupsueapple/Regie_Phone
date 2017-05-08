package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/8/13.
 */
public class ImageDrawable implements Serializable {

    private String name;

    public ImageDrawable(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
