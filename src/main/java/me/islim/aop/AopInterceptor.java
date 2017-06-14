package me.islim.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AopInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopInterceptor.class);

    private static final ThreadLocal<Long> EXECUTE_TIME_LOCAL = new ThreadLocal<Long>();

    public static void before(){
        EXECUTE_TIME_LOCAL.set(System.currentTimeMillis());
    }

    public static void after(String cName, String mName){
        long _cost = System.currentTimeMillis() - EXECUTE_TIME_LOCAL.get();
        LOGGER.info(cName + "." + mName + " Method cost : " + _cost);
    }

    public static void logSQL(String cName, String mName, String statement){
//        long _cost = System.currentTimeMillis() - EXECUTE_TIME_LOCAL.get();
        LOGGER.info(cName + "." + mName + " : " + statement);
    }

    public static void logException(String content){
        LOGGER.info(content);
    }
}
