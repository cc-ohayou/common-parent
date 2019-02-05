package com.ccspace.manager.common.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @AUTHOR CF
 * @DATE Created on 2018/10/24 15:13.
 */
public enum RedisChannel {
    //
    TEST_CHANNEL("cc-channel","测试用频道","cc-boot-demo"),
    TEST_CHANNEL02("cc-channel-02","测试用频道2","cc-boot-demo"),
    ;

    private String value;
    private String label;
    private String projectBelong;


    RedisChannel(String value, String label, String projectBelong) {
        this.value = value;
        this.label = label;
        this.projectBelong = projectBelong;
    }

    public String getProjectBelong() {
        return projectBelong;
    }

    public void setProjectBelong(String projectBelong) {
        this.projectBelong = projectBelong;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String[] getAllPsNeedSubChannels() {
        RedisChannel[] channelArrays= RedisChannel.values();
        List<String> list=new ArrayList<>(channelArrays.length);
        for(int i=0;i<channelArrays.length;i++){
            if(channelArrays[i].getProjectBelong().contains("cc-boot-demo")){
                list.add(channelArrays[i].getValue());
            }
        }
        String [] array=new String[list.size()];
        return list.toArray(array);
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(getAllPsNeedSubChannels()));
    }

}
