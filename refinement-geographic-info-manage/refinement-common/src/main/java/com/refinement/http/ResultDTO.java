package com.refinement.http;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class ResultDTO implements Serializable {

    // 返回结果状态码
    private int c;

    // 返回结果说明
    private String m;

    // 返回结果
    private Object r;


    public ResultDTO() {
        this.c = ResultEnum.SUCCESS.getCode();
        this.m = ResultEnum.SUCCESS.getMessage();
    }

    public ResultDTO(ResultEnum result){
        this.c = result.getCode();
        this.m = result.getMessage();
    }

    public ResultDTO(ResultEnum result, Object data){
        this.c = result.getCode();
        this.m = result.getMessage();
        this.r = data;
    }

    public ResultDTO(int code, String message) {
        this.c = code;
        this.m = message;
    }

    public ResultDTO(int code, String message, Object data) {
        this.c = code;
        this.m = message;
        this.r = data;
    }

    public ResultDTO(Object data) {
        this.c = ResultEnum.SUCCESS.getCode();
        this.m = ResultEnum.SUCCESS.getMessage();
        this.r = data;
    }

    public ResultDTO(Exception e) {
        this.c = ResultEnum.SERVER_EXCEPTION.getCode();
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        this.m = sw.toString();
        System.out.print(sw.toString());
    }

    public int getCode() {
        return c;
    }

    public void setCode(int code) {
        this.c = code;
    }

    public String getMessage() {
        return m;
    }

    public void setMessage(String message) {
        this.m = message;
    }

    public Object getResult() {
        return r;
    }

    public void setResult(Object result) {
        this.r = result;
    }

}
