package me.islim.xml;

import me.islim.AbstractConfigReader;
import me.islim.aop.MyPointcut;
import me.islim.aop.MyPointcutAdvisor;
import me.islim.io.ResourceLoader;
import me.islim.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;


public class XMLConfigReader extends AbstractConfigReader {

    public XMLConfigReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadConfig(String location) throws Exception {
        InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
        doLoadConfig(inputStream);
    }

    private void doLoadConfig(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        parseConfig(root);
        inputStream.close();
    }

    private void parseConfig(Element root) throws ClassNotFoundException {
        NodeList nl = root.getChildNodes();
        MyPointcutAdvisor[] _advisors = new MyPointcutAdvisor[nl.getLength()];
        int count = 0;
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                count++;
                Element ele = (Element) node;
                _advisors[i] = new MyPointcutAdvisor();
                processConfig(ele, _advisors[i]);
            }
        }
        MyPointcutAdvisor[] advisors = new MyPointcutAdvisor[count];
        int index = 0;
        for(MyPointcutAdvisor advisor : _advisors){
            if(index < count && advisor != null){
                advisors[index] = advisor;
                index++;
            }
        }
        setAdvisors(advisors);
    }

    private void processConfig(Element ele, MyPointcutAdvisor advisor) throws ClassNotFoundException {
        String className = ele.getAttribute("className");
        String methodName = ele.getAttribute("methodName");
        String adviceClass = ele.getAttribute("adapter");
        MyPointcut pointcut = new MyPointcut(className, methodName);
        advisor.setPointcut(pointcut);
        advisor.setAdviceClass(Class.forName(adviceClass));
    }

}
