package com.refinement.config;

/**
 * 线程单例
 */
public class MyThreadLocal extends ThreadLocal<Object> {

    private static volatile MyThreadLocal myThreadLocal = null;

    private MyThreadLocal() {

    }

    public static MyThreadLocal getInstance() {
        if (myThreadLocal == null) {
            synchronized (MyThreadLocal.class) {
                if (myThreadLocal == null) {
                    myThreadLocal = new MyThreadLocal();
                }
            }
        }
        return myThreadLocal;
    }

    public static Long getUserId() {
        Object o = getInstance().get();
        if (o != null) {
            return Long.parseLong(o + "");
        }
        return null;
    }
}
