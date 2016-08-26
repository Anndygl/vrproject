package com.example.sun.test2;
import java.io.Serializable;
public class Scenic implements Serializable {//景区简介item类标题，描述，图片
        private String title;
        private String desc;
        private int photoId;

        /**
         * Constructs a new instance of {@code Object}.
         */
        public Scenic(){};
        public Scenic(String name, String text, int photoId) {
        this.title=name;
        this.desc=text;
        this.photoId=photoId;
        }

        public void setDesc(String desc) {
        this.desc = desc;
        }

        public void setTitle(String title) {
        this.title = title;
        }

        public void setPhotoId(int photoId) {
        this.photoId = photoId;
        }

public String getDesc() {
        return desc;
        }

        public int getPhotoId() {
        return photoId;
        }

public String getTitle() {
        return title;
        }
        }