package com.administrator.hello.bean;

import com.google.gson.JsonElement;

public class Lolfree {
    public JsonElement champion;


    public Lolfree() {}

    public Lolfree(JsonElement bean) {
        this.champion = bean;
    }

    @Override
    public String toString() {
        return "Lolfree{" +
                "champion=" + champion +"}";
    }

}
