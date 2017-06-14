package me.islim.adapter;

import me.islim.aop.MyPointcutAdvisor;
import me.islim.aop.PointcutAdvisor;
import me.islim.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;


public class AddTimerAdapter extends ClassVisitor implements Advice {

    private String owner;
    private boolean isInterface;
    private PointcutAdvisor advisor;

    public AddTimerAdapter(ClassVisitor cv, PointcutAdvisor advisor){
        super(ASM4, cv);
        this.advisor = advisor;
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
            mv = new AddTimerMethodAdapter(mv, name);
        }

        return mv;
    }

    @Override
    public void visitEnd(){
        if(!isInterface){
            FieldVisitor fv = cv.visitField(ACC_PUBLIC + ACC_STATIC,
                    "timer", "J", null, null);
            if(fv != null)
                fv.visitEnd();
            cv.visitEnd();
        }
    }




    class AddTimerMethodAdapter extends MethodVisitor {

        private String mName;

        public AddTimerMethodAdapter(MethodVisitor mv){
            super(ASM4, mv);
        }

        public AddTimerMethodAdapter(MethodVisitor mv, String mName){
            super(ASM4, mv);
            this.mName = mName;
        }

        @Override
        public void visitCode(){
            mv.visitCode();

            // timer -= System.currentTimeMillis();
            mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
            mv.visitInsn(LSUB);
            mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

            // AopInterceptor.before();
            mv.visitMethodInsn(INVOKESTATIC, "me/islim/aop/AopInterceptor", "before", "()V");
        }

        @Override
        public void visitInsn(int opcode){
            if((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW){

                // timer += System.currentTimeMillis();
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
                mv.visitInsn(LADD);
                mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

                // System.out.println(timer);
                mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V");

                // AopInterceptor.after(className, methodName);
                mv.visitLdcInsn(owner.replace("/", "."));
                mv.visitLdcInsn(mName);
                mv.visitMethodInsn(INVOKESTATIC, "me/islim/aop/AopInterceptor",
                        "after", "(Ljava/lang/String;Ljava/lang/String;)V");
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals){
            mv.visitMaxs(maxStack + 4, maxLocals);
        }
    }
}


