package me.islim.aop;



public class MyPointcutAdvisor implements PointcutAdvisor {

    private Pointcut pointcut;

    private Class adviceClass;

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    public void setAdviceClass(Class adviceClass) {
        this.adviceClass = adviceClass;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Class getAdviceClass() {
        return adviceClass;
    }
}
