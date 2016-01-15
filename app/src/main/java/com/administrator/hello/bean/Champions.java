
package com.administrator.hello.bean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Champions {

    @SerializedName("champions")
    @Expose
    private List<Champion> champions = new ArrayList<Champion>();

    /**
     * 
     * @return
     *     The champions
     */
    public List<Champion> getChampions() {
        return champions;
    }

    /**
     * 
     * @param champions
     *     The champions
     */
    public void setChampions(List<Champion> champions) {
        this.champions = champions;
    }

    @Override
    public String toString() {
        //return ToStringBuilder.reflectionToString(this);
        String str = "[";
        for(int i=0; i < champions.size(); i++){
            str += elementData[i];
            if(i < size-1){
                str += ",";
            }
        }
        return str + "]";

    }

}
