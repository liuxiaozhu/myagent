package me.islim.adapter;

import org.objectweb.asm.ClassVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.V1_6;


public class ChangeVersionAdapter extends ClassVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeVersionAdapter.class);

    public ChangeVersionAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }

    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces){
        LOGGER.info(name + " version: " + version);
        cv.visit(V1_6, access, name, signature, superName, interfaces);
    }
}
