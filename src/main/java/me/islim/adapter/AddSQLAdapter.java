package me.islim.adapter;

import me.islim.aop.MyPointcutAdvisor;
import me.islim.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;


public class AddSQLAdapter extends ClassVisitor implements Advice {

    private String owner;
    private boolean isInterface;
    private PointcutAdvisor advisor;

    public AddSQLAdapter(ClassVisitor cv, PointcutAdvisor advisor){
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

        if(!isInterface && mv != null && !name.equals("<init>")
                && advisor.getPointcut().getMethodMatcher().matches(name, owner)) {
            mv = new AddSQLMethodAdapter(mv, name);
        }
        return mv;
    }

    @Override
    public void visitEnd(){
        if(!isInterface){
            cv.visitEnd();
        }
    }




    class AddSQLMethodAdapter extends MethodVisitor {

        private String mName;

        public AddSQLMethodAdapter(MethodVisitor mv){
            super(ASM4, mv);
        }

        public AddSQLMethodAdapter(MethodVisitor mv, String mName){
            super(ASM4, mv);
            this.mName = mName;
        }

        @Override
        public void visitCode(){
            mv.visitCode();

            mv.visitLdcInsn(owner.replace("/", "."));
            mv.visitLdcInsn(mName);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "me/islim/aop/AopInterceptor",
                        "logSQL", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
        }

        @Override
        public void visitInsn(int opcode){
            if((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW){

            }
            mv.visitInsn(opcode);
        }

        public void visitMaxs(int maxStack, int maxLocals){
            mv.visitMaxs(maxStack + 4, maxLocals);
        }

    }
}


