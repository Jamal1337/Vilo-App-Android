package com.fabian.vilo.models;

/**
 * Created by Fabian on 25/10/15.
 */
public class ViloResponse {
    private boolean error;
    private String msg;

    public boolean isError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
