package com.htec.microservices.common.util;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

public class CommonUtil {

    private static class CommonUtilHolder {
        static final CommonUtil INSTANCE = new CommonUtil();
    }

    public static CommonUtil getInstance() {
        return CommonUtil.CommonUtilHolder.INSTANCE;
    }

    public static <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
