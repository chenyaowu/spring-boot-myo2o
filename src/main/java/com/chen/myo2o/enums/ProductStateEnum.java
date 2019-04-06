package com.chen.myo2o.enums;

public enum ProductStateEnum {
    SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),EMPTY_LIST(-1002,"添加数小于1"), EMPTY(-1003, "商品或店铺id为空");
    private int state;
    private String stateInfo;

    ProductStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }
    public static ProductStateEnum stateOf(int state){
        for (ProductStateEnum productStateEnum : values()){
            if(productStateEnum.getState() == state){
                return  productStateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }
}
