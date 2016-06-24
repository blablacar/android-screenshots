package com.comuto

/**
 * Holds data about device on which we take screenshots.*/
public class DeviceDetails {

    String type
    String serialNo

    DeviceDetails(String type, String serialNo) {
        this.type = type
        this.serialNo = serialNo
    }
}
