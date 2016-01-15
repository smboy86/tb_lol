
package com.administrator.hello.bean;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Generated("org.jsonschema2pojo")
public class Champion {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("botEnabled")
    @Expose
    private Boolean botEnabled;
    @SerializedName("freeToPlay")
    @Expose
    private Boolean freeToPlay;
    @SerializedName("botMmEnabled")
    @Expose
    private Boolean botMmEnabled;
    @SerializedName("rankedPlayEnabled")
    @Expose
    private Boolean rankedPlayEnabled;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * 
     * @return
     *     The botEnabled
     */
    public Boolean getBotEnabled() {
        return botEnabled;
    }

    /**
     * 
     * @param botEnabled
     *     The botEnabled
     */
    public void setBotEnabled(Boolean botEnabled) {
        this.botEnabled = botEnabled;
    }

    /**
     * 
     * @return
     *     The freeToPlay
     */
    public Boolean getFreeToPlay() {
        return freeToPlay;
    }

    /**
     * 
     * @param freeToPlay
     *     The freeToPlay
     */
    public void setFreeToPlay(Boolean freeToPlay) {
        this.freeToPlay = freeToPlay;
    }

    /**
     * 
     * @return
     *     The botMmEnabled
     */
    public Boolean getBotMmEnabled() {
        return botMmEnabled;
    }

    /**
     * 
     * @param botMmEnabled
     *     The botMmEnabled
     */
    public void setBotMmEnabled(Boolean botMmEnabled) {
        this.botMmEnabled = botMmEnabled;
    }

    /**
     * 
     * @return
     *     The rankedPlayEnabled
     */
    public Boolean getRankedPlayEnabled() {
        return rankedPlayEnabled;
    }

    /**
     * 
     * @param rankedPlayEnabled
     *     The rankedPlayEnabled
     */
    public void setRankedPlayEnabled(Boolean rankedPlayEnabled) {
        this.rankedPlayEnabled = rankedPlayEnabled;
    }

    @Override
    public String toString() {
        //return ToStringBuilder.reflectionToString(this);
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
