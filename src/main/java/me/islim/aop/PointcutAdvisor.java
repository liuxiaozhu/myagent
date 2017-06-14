package me.islim.aop;



public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}
