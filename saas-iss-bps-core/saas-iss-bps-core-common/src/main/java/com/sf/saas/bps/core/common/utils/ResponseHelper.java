package com.sf.saas.bps.core.common.utils;

import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.dto.base.AppResponse;
import com.sf.saas.bps.core.dto.base.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author 01407460
 * @date 2022/9/7 10:44
 */
@Slf4j
public class ResponseHelper<T> {

    private static final String PACKAGE_NAME = "com.sf.saas.bps";

    public static <T> Response<T> buildSuccess() {
        ResponseCodeEnum success = ResponseCodeEnum.SUCCESS;
        Response<T> response = build(true, success.getCode(), success.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }

    public static <T> Response<T> buildSuccess(T result) {
        ResponseCodeEnum success = ResponseCodeEnum.SUCCESS;
        Response<T> response = build(true, success.getCode(), success.getMsg(), result);
        response.setTraceId(getTranceId());
        return response;
    }
    public static <T> Response<T> buildFail() {
        ResponseCodeEnum failure = ResponseCodeEnum.FAILURE;
        Response<T> response = build(false, failure.getCode(), failure.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }
    public static <T> Response<T> buildFailWithLog() {
        ResponseCodeEnum failure = ResponseCodeEnum.FAILURE;
        Response<T> response = build(false, failure.getCode(), failure.getMsg(), null);
        response.setTraceId(getTranceId());
        printErrorInfo(response.getCode());
        return response;
    }

    public static <T> Response<T> buildFail(ResponseCodeEnum codeEnum) {
        Response<T> response = build(false, codeEnum.getCode(), codeEnum.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }
    public static <T> Response<T> buildFailWithErrorData(ResponseCodeEnum codeEnum, T data) {
        Response<T> response = build(false, codeEnum.getCode(), codeEnum.getMsg(), data);
        response.setTraceId(getTranceId());
        return response;
    }
    public static <T> Response<T> buildFail(String code,String msg) {
        Response<T> response = build(false, code, msg, null);
        response.setTraceId(getTranceId());
        return response;
    }
    public static <T> Response<T> buildFailWithLog(String code,String msg) {
        Response<T> response = build(false, code, msg, null);
        response.setTraceId(getTranceId());
        return response;
    }

    public static <T> Response<T> buildFailWithLog(ResponseCodeEnum codeEnum) {
        Response<T> response = build(false, codeEnum.getCode(), codeEnum.getMsg(), null);
        response.setTraceId(getTranceId());
        printErrorInfo(response.getCode());
        return response;
    }




    private static <T> Response<T> build(boolean succ,  String code,String msg,T result) {
        Response<T> resp = new Response<>();
        resp.setData(result);
        resp.setSuccess(succ);
        resp.setCode(code);
        resp.setMsg(msg);
        return resp;
    }


    /**
     * APP??????????????????
     * @param <T>
     * @return
     */
    public static <T> AppResponse<T> buildAppSuccess() {
        ResponseCodeEnum success = ResponseCodeEnum.SUCCESS;
        AppResponse<T> response = buildApp(true, success.getCode(), success.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }

    public static <T> AppResponse<T> buildAppSuccess(T result) {
        ResponseCodeEnum success = ResponseCodeEnum.SUCCESS;
        AppResponse<T> response = buildApp(true, success.getCode(), success.getMsg(), result);
        response.setTraceId(getTranceId());
        return response;
    }

    public static <T> AppResponse<T> buildAppFail(ResponseCodeEnum codeEnum) {
        AppResponse<T> response = buildApp(false, codeEnum.getCode(), codeEnum.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }

    public static <T> AppResponse<T> buildAppFail() {
        ResponseCodeEnum failure = ResponseCodeEnum.FAILURE;
        AppResponse<T> response = buildApp(false, failure.getCode(), failure.getMsg(), null);
        response.setTraceId(getTranceId());
        return response;
    }

    private static <T> AppResponse<T> buildApp(boolean succ,  String code,String msg,T result) {
        AppResponse<T> resp = new AppResponse<>();
        resp.setObj(result);
        resp.setSuccess(succ);
        resp.setCodeMsg(code);
        resp.setMsg(msg);
        return resp;
    }




    /**
     * ?????? ?????????????????? ?????????????????? ???????????????????????????????????????
     * ????????????????????????
     * ??????????????????
     * @param code
     */
    private static void printErrorInfo(String code){
        //
        String format = "\tat %s.%s(%s.java:%s)";
        try {
            String lineSeparator = System.lineSeparator();
            Throwable ex = new Throwable();
            StackTraceElement[] stackElements = ex.getStackTrace();
            String errInfo = Arrays.stream(stackElements)
                    .filter(e -> e.getClassName().startsWith(PACKAGE_NAME))
                    .map(e -> String.format(format, e.getClassName(),
                            e.getMethodName(),
                            StringUtils.substring(e.getClassName(), e.getClassName().lastIndexOf(BpsConstant.SPLIT_SPOT)+1),
                            e.getLineNumber()))
                    .collect(Collectors.joining(lineSeparator));

            log.error(":{},{}",code,errInfo);

        } catch (Exception e) {
//            log.error("?????????????????????",e);
        }
    }



    private static String getTranceId(){
        try {
          return   MDC.get(BpsConstant.TRACE_ID);
        } catch (IllegalArgumentException e) {
        }
        return null;
    }

}
