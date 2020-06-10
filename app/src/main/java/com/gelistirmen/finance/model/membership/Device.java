package com.gelistirmen.finance.model.membership;

public class Device
{
    public String manufacturer;
    public String model;
    public String os;
    public String osVersion;
    public String token;

    public Device(String manufacturer, String model, String os, String osVersion, String token) {
        this.manufacturer = manufacturer;
        this.model = model;
        this.os = os;
        this.osVersion = osVersion;
        this.token = token;
    }
}
