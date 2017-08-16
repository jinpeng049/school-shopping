package com.zj.shop.controller;

import com.zj.shop.model.Response;
import com.zj.shop.utils.ShoppingLogs;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @auther Administrator jin
 * @create 2017/6/15
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final ShoppingLogs SHOPPING_LOGS = ShoppingLogs.getLogger(ControllerExceptionHandler.class);

    /**
     * 全局处理Exception
     * 错误的情况下返回500
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public Response handleOtherExceptions(Exception ex, HttpServletRequest request) {
        SHOPPING_LOGS.error("uri :" + request.getServletPath() + "exception", ex);
        return new Response(Response.FAILURE, ex.getMessage());
    }
}
