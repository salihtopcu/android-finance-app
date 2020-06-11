package com.gelistirmen.finance.model.operation;

import android.util.Log;

import com.gelistirmen.finance.util.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Fingerprint implements Serializable {
    public String value;
    public int timeout;

    public Fingerprint(String value, int timeout) {
        this.value = value;
        this.timeout = timeout;
    }

    public Fingerprint(JSONObject jsonObject) {
        try {
            this.value = Method.getString(jsonObject, "fingerPrint");
            this.timeout = Method.getInt(jsonObject, "timeout");
        } catch (JSONException e) {
            Log.e("Fingerprint", "Json parse error.");
        }
    }
}
