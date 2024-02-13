package com.charter.agentOs.model;

import lombok.Data;

@Data
public class DeviceDetails {

    private long deviceId;
    private long imei;
    private String color;
    private String os;
    private boolean isSimCapable;
    private boolean wearable;

}
