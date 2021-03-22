package com.chen.conductorbackend.enums;

public enum LostStatus {
    //四种状态：未受理，进行中，已完成，已超时
    UNACCEPTED(0, "未受理"),
    IN_PROGRESS(1, "进行中"),
    FINISHED(2, "已完成"),
    OVERDUE(3, "已超时");
    private Integer status;
    private String name;

    LostStatus(Integer status, String name) {
        this.status = status;
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //检查一种状态是否在枚举类中存在
    public static boolean isExist(Integer status) {
        for (LostStatus lostStatus : LostStatus.values()) {
            if (status == lostStatus.getStatus()) {
                return true;
            }
        }
        return false;
    }

    //已知枚举类型代表的整数，返回中文状态
    public static String nameOf(Integer status) {
        for (LostStatus lostStatus : LostStatus.values()) {
            if (status == lostStatus.getStatus()) {
                return lostStatus.getName();
            }
        }
        return "";
    }

}
