package com.refinement.http;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;

/**
 * The class Wrapper.
 *
 * @param <T> the type parameter
 */
@Data
public class Wrapper<T> implements Serializable {

    /**
     * 编号.
     */
    @ApiModelProperty("响应code")
    private int code;

    /**
     * 信息.
     */
    @ApiModelProperty("提示信息")
    private String message;

    /**
     * 结果数据
     */
    @ApiModelProperty("响应数据")
    private T result;

    /**
     * Instantiates a new wrapper. default code=200
     */
    public Wrapper() {
        this(DefaultResponseCode.SUCCESS);
    }

    public Wrapper(ResponseCode responseCode) {
        this.code(responseCode.getCode()).message(responseCode.getMessage());
    }

    public Wrapper(ResponseCode responseCode, Object... args) {
        this(responseCode, null, args);
    }

    public Wrapper(ResponseCode responseCode, T result, Object... args) {
        this.result = result;
        this.code = responseCode.getCode();
        FormattingTuple tuple = MessageFormatter.arrayFormat(responseCode.getMessage(), args);
        this.message = tuple.getMessage();
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param responseCode the responseCodeEnum
     * @param result       the result
     */
    public Wrapper(ResponseCode responseCode, T result) {
        super();
        this.code(responseCode.getCode()).message(responseCode.getMessage()).result(result);
    }

    protected Wrapper(int code, String message) {
        this.code(code).message(message).result(result);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param code the new 编号
     * @return the wrapper
     */
    private Wrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 , 返回自身的引用.
     *
     * @param message the new 信息
     * @return the wrapper
     */
    private Wrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 , 返回自身的引用.
     *
     * @param result the new 结果数据
     * @return the wrapper
     */
    public Wrapper<T> result(T result) {
        this.setResult(result);
        return this;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    @JsonIgnore
    public boolean success() {
        return DefaultResponseCode.SUCCESS.getCode() == this.code;
    }

    /**
     * 判断是否成功： 依据 Wrapper.SUCCESS_CODE != this.code
     *
     * @return code !=200,true;否则 false.
     */
    @JsonIgnore
    public boolean error() {
        return !success();
    }

}
