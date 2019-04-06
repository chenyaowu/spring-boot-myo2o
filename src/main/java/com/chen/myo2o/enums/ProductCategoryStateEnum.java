package com.chen.myo2o.enums;

public enum ProductCategoryStateEnum {
   SUCCESS(1, "操作成功"), INNER_ERROR(-1001, "内部系统错误"),EMPTY_LIST(-1002,"添加数小于1");
    private int state;
    private String stateInfo;

    ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /*
    依据传入的state返回相应的enum值
     */
    public static ProductCategoryStateEnum stateOf(int state) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == state)
                return productCategoryStateEnum;
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