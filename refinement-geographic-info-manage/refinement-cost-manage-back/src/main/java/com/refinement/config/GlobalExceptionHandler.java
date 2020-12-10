package com.refinement.config;

import com.refinement.http.DefaultResponseCode;
import com.refinement.http.ResponseCode;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     *
     * @param e e
     * @return Wrapper
     */
    @ExceptionHandler(BusinessException.class)
    public Wrapper<?> businessException(BusinessException e) {
        ResponseCode responseCode = e.getResponseCode();
        responseCode = responseCode == null ? DefaultResponseCode.ERROR : responseCode;
        log.error("业务异常", e);
        Object[] orbs = e.getArgs();
        return WrapMapper.error(responseCode, orbs);
    }

    /**
     * 参数异常
     *
     * @param e e
     * @return Wrapper
     */
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            BindException.class})
    public Wrapper<?> handleValidationException(Exception e) {
        String msg;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException t = (MethodArgumentNotValidException) e;
            msg = t.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        } else if (e instanceof BindException) {
            BindException t = (BindException) e;
            msg = t.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            msg = t.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            msg = t.getParameterName() + " 不能为空";
        } else if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            msg = t.getVariableName() + " 不能为空";
        } else {
            msg = "必填参数缺失";
        }
        log.warn("参数校验不通过,msg: {}", msg);
        return WrapMapper.illegalArgument(msg);
    }

    @ExceptionHandler(Exception.class)
    public Wrapper<?> exception(Exception e) {
        log.error("发生异常, e={}", e.getMessage(), e);
        return WrapMapper.wrap(DefaultResponseCode.ERROR, e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Wrapper<?> handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return WrapMapper.wrap(DefaultResponseCode.ERROR);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Wrapper<?> handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return WrapMapper.error("数据库中已存在该记录");
    }
}
