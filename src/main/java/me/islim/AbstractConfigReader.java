package me.islim;

import me.islim.aop.MyPointcutAdvisor;
import me.islim.aop.PointcutAdvisor;
import me.islim.io.ResourceLoader;
import me.islim.io.ResourceLoader;

import java.util.HashMap;


public abstract class AbstractConfigReader implements ConfigReader {

    private ResourceLoader resourceLoader;
    private MyPointcutAdvisor[]  advisors;

    protected AbstractConfigReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setAdvisors(MyPointcutAdvisor[] advisors){
        this.advisors = advisors;
    }

    public PointcutAdvisor[] getAdvisors() {
        return advisors;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
