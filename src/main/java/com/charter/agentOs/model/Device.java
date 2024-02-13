package com.charter.agentOs.model;

import lombok.Data;

@Data
public class Device {

    private long deviceId;
    private String deviceType;
    private String deviceInformation;
    private boolean hasDpp;
    private DeviceDetails deviceDetails;
    private long totalPrice;


}
