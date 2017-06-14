package me.islim.aop;


public class MyPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private String className;
    private String methodName;

    public MyPointcut(String className, String methodName){
        this.className = className;
        this.methodName = methodName;
    }

    public boolean matches(String targetClass) {
        if(targetClass.contains(className))
            return true;
        return false;
    }

    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    public boolean matches(String method, String targetClass) {
        if(targetClass.contains(className))
            if(method.contains(methodName))
                return true;
        return false;
    }
}
