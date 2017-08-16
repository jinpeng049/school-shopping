package com.zj.shop.model;

public class Response<T> {

    //Borrow from HTTP
    public static final int SUCCESS = 1001;

    public static final int FAILURE = 1002;

    private int status;

    private String message;

    private T data;

    public Response() {
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //    //增加异常处理方式
    //    public static Response<String> getUnKnownRes(Exception e){
    //        Response<String> rst = new Response<>();
    //        rst.setMessage(ExceptionUtil.getReadableInfo(e));
    //        rst.setStatus(Response.FAILURE);
    //        rst.setData(ExceptionUtil.processExcep(e));
    //        return rst;
    //    }
    //    // add for empty ok
    //    public static Response getEmptyOkRes(String msg){
    //        Response rst = new Response();
    //        rst.setStatus(Response.SUCCESS);
    //        rst.setMessage(msg);
    //        return rst;
    //    }


    @Override
    public String toString() {
        return "Response{" + "status=" + status + ", message='" + message + '\'' + ", data=" + data + '}';
    }
}

