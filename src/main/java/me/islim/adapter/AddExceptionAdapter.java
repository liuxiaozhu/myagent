package me.islim.adapter;

import me.islim.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;


public class AddExceptionAdapter extends ClassVisitor implements Advice {

    private String owner;
    private boolean isInterface;
    private PointcutAdvisor advisor;

    public AddExceptionAdapter(ClassVisitor cv, PointcutAdvisor advisor) {
        super(ASM4, cv);
        this.advisor =  advisor;
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

        if(!isInterface && mv != null // && !name.equals("<init>")
                && advisor.getPointcut().getMethodMatcher().matches(name, owner)) {
            mv = new AddExceptionAdapter.AddExceptionMethodAdapter(mv, name);
        }
        return mv;
    }

    @Override
    public void visitEnd(){
        if(!isInterface){
            cv.visitEnd();
        }
    }

    class AddExceptionMethodAdapter extends MethodVisitor {

        private String mName;

        public AddExceptionMethodAdapter(MethodVisitor mv){
            super(ASM4, mv);
        }

        public AddExceptionMethodAdapter(MethodVisitor mv, String mName){
            super(ASM4, mv);
            this.mName = mName;
        }

        @Override
        public void visitCode(){
            mv.visitCode();
        }

        @Override
        public void visitInsn(int opcode){
            if(opcode == ATHROW){
//                mv.visitVarInsn(ALOAD, 2);
                mv.visitLdcInsn("aaaaaaaaaaaaaaaaaaaa");
                mv.visitMethodInsn(INVOKESTATIC, "me/islim/aop/AopInterceptor",
                        "logException", "(Ljava/lang/String;)V");

            }
            mv.visitInsn(opcode);
        }

        public void visitMaxs(int maxStack, int maxLocals){
            mv.visitMaxs(maxStack + 4, maxLocals);
        }

    }

}
