package me.islim;

import me.islim.aop.MyPointcutAdvisor;
import me.islim.aop.PointcutAdvisor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;


public class Transformer implements ClassFileTransformer {

    private static PointcutAdvisor[] advisors;

    public static void setAdvisors(PointcutAdvisor[] advisors1){
        advisors = advisors1;
    }

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
//                System.out.println(loader);

        // 解析Class
//                if(className.contains("AdminController")){
//                    ClassReader reader = new ClassReader(classfileBuffer);
//                    ClassPrinter visitor = new ClassPrinter();
//                    reader.accept(visitor, 0);
//                    return null;
//                }

        // 修改Version
//                if(className.contains("AdminController")){
//                    ClassReader reader = new ClassReader(classfileBuffer);
//                    ClassWriter writer = new ClassWriter(reader, 0);
//                    ChangeVersionAdapter visitor = new ChangeVersionAdapter(writer);
//                    reader.accept(visitor, 0);
//                    return writer.toByteArray();
//                }

        // 删除方法
//                if(className.contains("AdminController")){
//                    ClassReader reader = new ClassReader(classfileBuffer);
//                    ClassWriter writer = new ClassWriter(reader, 0);
//                    RemoveMethodAdapter visitor = new RemoveMethodAdapter(writer,
//                            "listAdmin",
//                            "(Lorg/springframework/ui/ModelMap;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/lang/String;");
//                    reader.accept(visitor, 0);
//                    return writer.toByteArray();
//                }

        // 添加成员 & TraceClassVisitor
//                if(className.contains("AdminController")){
//                    ClassReader reader = new ClassReader(classfileBuffer);
//                    ClassWriter writer = new ClassWriter(reader, 0);
//                    AddFieldAdapter visitor = new AddFieldAdapter(writer,
//                            ACC_PRIVATE + ACC_FINAL + ACC_STATIC,
//                            "MYFIELD",
//                            "Ljava/lang/String;",
//                            "aaa");
//
//                    TraceClassVisitor cv = new TraceClassVisitor(visitor, new PrintWriter(System.out, true));
//
//                    cv.visitField(ACC_PRIVATE + ACC_FINAL + ACC_STATIC, "MYFIELD", "Ljava/lang/String;", null, null);
//                    cv.visitEnd();
//
//                    reader.accept(visitor, 0);
//                    return writer.toByteArray();
//                }

//        // 添加 Timer
//        if(className.contains("AdminController")
//                || className.contains("AdminService")){
//            ClassReader reader = new ClassReader(classfileBuffer);
//            ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES);
//            AddTimerAdapter visitor = new AddTimerAdapter(writer);
//            reader.accept(visitor, 0);
//            return writer.toByteArray();
//        }
//
//        // 添加 SQL
//        if(className.contains("SqlSessionTemplate")){
//            ClassReader reader = new ClassReader(classfileBuffer);
//            ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES);
//            AddSQLAdapter visitor = new AddSQLAdapter(writer);
//            reader.accept(visitor, 0);
//            return writer.toByteArray();
//        }

        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES);
        for(PointcutAdvisor advisor : advisors){
            if(advisor.getPointcut().getClassFilter().matches(className)){
//                AddSQLAdapter visitor = new AddSQLAdapter(writer);
                Constructor constructor = null;
                try {
                    constructor = advisor.getAdviceClass()
                            .getDeclaredConstructor(ClassVisitor.class, PointcutAdvisor.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                constructor.setAccessible(true);
                ClassVisitor visitor = null;
                try {
                     visitor = (ClassVisitor) constructor.newInstance(writer, advisor);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                reader.accept(visitor, 0);
                return writer.toByteArray();
            }
        }

        return null;
    }

}
