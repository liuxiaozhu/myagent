package me.islim.adapter;

import me.islim.Agent;
import me.islim.Agent;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.objectweb.asm.Opcodes.ASM4;

/**
 * Parsing Classes
 */
public class ClassPrinter extends ClassVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class);

    public ClassPrinter(){
        super(ASM4);
    }

    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces){
        LOGGER.info(name + " extends " + superName + " {");
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {

        LOGGER.info("   field access : " + access);

        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {

        LOGGER.info("   " + name + " " + desc);


        return null;
    }

    public void visitEnd(){
        LOGGER.info("}");
    }


}
