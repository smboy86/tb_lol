package com.administrator.hello.bean;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lolfree {
    private List<ChampionList> champions;

    public List<ChampionList> getChampionListList(){
        return champions;
    }

    public class ChampionList{
        private String id;
        private String active;
        private String botEnabled;
        private String freeToPlay;
        private String botMmEnabled;
        private String rankedPlayEnavled;

        public String getId(){
            return id;
        }

        public String getActive(){
            return active;
        }

        public String getBotEnabled(){
            return botEnabled;
        }

        public String getFreeToPlay(){
            return freeToPlay;
        }

        public String getBotMmEnabled(){
            return botMmEnabled;
        }

        public String getRankedPlayEnavled(){
            return rankedPlayEnavled;
        }

    }

    @Override
    public String toString() {
        return "Lolfree{" +
                "champion=" + champions + "}";

    }

    public String toString(String a) {
        return "";
    }

    public String toString(int index) {
        return "\n\nNo " + index + " champion "
                + "id : " + champions.get(index).getId()
                + " active : " + champions.get(index).getActive()
                + " botEnabled : " + champions.get(index).getBotEnabled()
                + " freeToPlay : " + champions.get(index).getFreeToPlay()
                + " botMmEnabled : " + champions.get(index).getBotMmEnabled()
                + " rankedPlayEnabled : " + champions.get(index).getRankedPlayEnavled();
    }

}