package com.chen.conductorbackend.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String wxId;
    private String username;
    private String phone;
    private Integer birth;
    private String gender;
    private String address;
    private Double longitude;
    private Double latitude;
    private Long gmtCreate;
    private Long gmtModified;
}
