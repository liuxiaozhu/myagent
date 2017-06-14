package me.islim;



import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

import me.islim.adapter.AddSQLAdapter;
import me.islim.adapter.AddTimerAdapter;
import me.islim.aop.MyPointcut;
import me.islim.aop.MyPointcutAdvisor;
import me.islim.aop.Pointcut;
import me.islim.aop.PointcutAdvisor;
import me.islim.io.ResourceLoader;
import me.islim.xml.XMLConfigReader;
import me.islim.io.ResourceLoader;
import me.islim.xml.XMLConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Agent {
    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

//    private static List<String> includeClassNames = new ArrayList<String>();
//
//    private static List<String> methodNames = new ArrayList<String>();

    public static void premain(String agentArgs, Instrumentation inst) throws Exception {

//        ClassDefinition def = new ClassDefinition(AdminController.class, Transformer
//                .getBytesFromFile(Transformer.classNumberReturns2));
//        inst.redefineClasses(new ClassDefinition[] { def });
//
//        for(Class c : inst.getAllLoadedClasses()){
//            System.out.println(c.getClassLoader());
//        }
//
//         注册
//        includeClassNames.add("AdminController");
//        includeClassNames.add("SqlSessionTemplate");
//        methodNames.add("login");
//        methodNames.add("select");
//
//        Pointcut controllerPointcut = new MyPointcut(includeClassNames.get(0), methodNames.get(0));
//        Pointcut sqlPointcut = new MyPointcut(includeClassNames.get(1), methodNames.get(1));
//
//        MyPointcutAdvisor controllerPointcutAdvisor = new MyPointcutAdvisor();
//        controllerPointcutAdvisor.setPointcut(controllerPointcut);
//        controllerPointcutAdvisor.setAdviceClass(AddTimerAdapter.class);
//        MyPointcutAdvisor sqlPointAdvisor = new MyPointcutAdvisor();
//        sqlPointAdvisor.setPointcut(sqlPointcut);
//        sqlPointAdvisor.setAdviceClass(AddSQLAdapter.class);
//
//        PointcutAdvisor[]  advisors = new PointcutAdvisor[2];
//        advisors[0] = controllerPointcutAdvisor;
//        advisors[1] = sqlPointAdvisor;

        XMLConfigReader xmlConfigReader = new XMLConfigReader(new ResourceLoader());
        xmlConfigReader.loadConfig("config.xml");

        Transformer transformer = new Transformer();
        transformer.setAdvisors(xmlConfigReader.getAdvisors());

        inst.addTransformer(transformer);
    }
}
