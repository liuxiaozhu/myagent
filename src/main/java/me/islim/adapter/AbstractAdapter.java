package me.islim.adapter;

import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM4;


public abstract class AbstractAdapter extends ClassVisitor {

    public AbstractAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }

}
