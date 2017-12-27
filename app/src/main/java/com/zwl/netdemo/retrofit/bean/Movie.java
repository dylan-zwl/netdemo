package com.zwl.netdemo.retrofit.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class Movie {
    private String title;
    private List<Subjects> subjects;


    public String getTitle() {
        return title;
    }

    public List<Subjects> getSubjects() {
        return subjects;
    }
}

