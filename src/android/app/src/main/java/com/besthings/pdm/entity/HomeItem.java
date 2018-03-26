package com.besthings.pdm.entity;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/12.
 */
public class HomeItem implements Serializable, Comparable<HomeItem> {
    private int id;
    private String name;
    private int img;
    private boolean lock;

    public HomeItem(int id, String name, int img) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.lock = false;
    }

    public HomeItem(int id, String name, int img, boolean lock) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.lock = lock;
    }


    public HomeItem(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public int compareTo(@NonNull HomeItem homeItem) {
        if (this.id < homeItem.id)
            return -1;
        if (this.id > homeItem.id)
            return 1;
        return 0;
    }
}
