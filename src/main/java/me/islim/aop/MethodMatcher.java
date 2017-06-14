package me.islim.aop;

import java.lang.reflect.Method;


public interface MethodMatcher {
    boolean matches(String method, String targetClass);
}
