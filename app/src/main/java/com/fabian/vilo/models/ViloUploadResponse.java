package com.fabian.vilo.models;

/**
 * Created by Fabian on 25/10/15.
 */
public class ViloUploadResponse {
    public boolean error;
    public String msg;
    public Result result;

    public class Result {
        public int id;
        public String attachment;

    }
}
