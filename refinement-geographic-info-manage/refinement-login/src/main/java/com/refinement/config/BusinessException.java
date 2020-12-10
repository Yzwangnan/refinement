package com.refinement.config;


import com.refinement.http.ResponseCode;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private Object[] args;

    private ResponseCode responseCode;

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public BusinessException(ResponseCode responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public BusinessException(ResponseCode responseCode, Throwable c) {
        super(c);
        this.responseCode = responseCode;
    }

    /**
     * @param responseCode message like "第{}行的{}不能为空"
     * @param args         替换responseCode中message的{}
     */
    public BusinessException(ResponseCode responseCode, Object... args) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.args = args;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public Object[] getArgs() {
        return args;
    }
}
