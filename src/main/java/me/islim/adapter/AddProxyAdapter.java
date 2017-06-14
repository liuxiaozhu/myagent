package me.islim.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;


public class AddProxyAdapter extends ClassVisitor {

    private String owner;
    private boolean isInterface;

    public AddProxyAdapter(ClassVisitor cv) {
        super(ASM4, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces){
        cv.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions){
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        if(!isInterface && mv != null && !name.equals("<init>"))
            mv = new AddProxyMethodAdapter(mv, name);

        return mv;
    }


    private class AddProxyMethodAdapter extends MethodVisitor{

        private String mName;

        public AddProxyMethodAdapter(MethodVisitor mv, String mName) {
            super(ASM4, mv);
            this.mName = mName;
        }

        @Override
        public void visitCode(){
            mv.visitCode();

        }

        @Override
        public void visitInsn(int opcode){
            if((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW){

            }
            mv.visitInsn(opcode);
        }
    }
}
