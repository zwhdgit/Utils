package com.zwh.asm_test;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_NATIVE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTSTATIC;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class MethodCostTimeClassVisitor extends ClassVisitor {

    private String owner;
    private boolean isInterface;
    private boolean hasTimer;

    public MethodCostTimeClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    public MethodCostTimeClassVisitor(int api) {
        super(api);
    }

    @Override
    public void visit(
            int version, int access, String name, String signature, String superName, String[] interfaces
    ) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & ACC_INTERFACE) != 0;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if ((access & ACC_STATIC) != 0 && "timer".equals(name) && "J".equals(descriptor)) {
            hasTimer = true;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String descriptor, String signature, String[] exceptions
    ) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!isInterface && mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
            boolean isAbstractMethod = (access & ACC_ABSTRACT) != 0; // 抽象
            boolean isNativeMethod = (access & ACC_NATIVE) != 0;    // native
            if (!isAbstractMethod && !isNativeMethod) {
                mv = new MethodCostTimeAdapter(api, mv, access, name, descriptor, owner, hasTimer);
                mv = new MethodEnterAndExitAdapter(api, mv);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (!isInterface && !hasTimer) {
            FieldVisitor fv = super.visitField(ACC_PUBLIC | ACC_STATIC, "timer", "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }


    /**
     * 首先要做的是找到方法开始和结束的时机，根据上面对 MethodVisit 的生命周期方法的介绍，
     * 可以把 visitCode 作为方法的开始，但是不能把 visitMaxs 当做方法的结束，因为 visitMaxs
     * 包含了 return 语句，那么换个思路，只要在 visitInsn 找到了方法退出的指令就可以了，
     * 方法退出的指令有 return（正常退出） 和 throw（异常退出）。实现代码如下：
     */
    public class MethodEnterAndExitAdapter extends MethodVisitor {

        public MethodEnterAndExitAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        /**
         * 方法开始访问时
         */
        @Override
        public void visitCode() {
            // 1.首先处理自己的代码逻辑
            // MethodEnter...
            // 2.然后调用父类的方法实现
            super.visitInsn(LCONST_0);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LSUB);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            // 1.首先处理自己的代码逻辑
            if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
                // MethodExit...
                super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                super.visitInsn(LADD);
                super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

                super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                // 创建一个 StringBuilder
                super.visitTypeInsn(NEW, "java/lang/StringBuilder");
                super.visitInsn(DUP);
                super.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
                // append name
                super.visitLdcInsn(owner + "." + "getName()" + " cost time:");
                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
                // append timer value
                super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;",
                        false);

                super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
                super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            // 2.然后调用父类的方法实现
            super.visitInsn(opcode);
        }
    }

    private static class MethodCostTimeAdapter extends AdviceAdapter {

        private String owner;
        private Boolean hasInjectCode;

        protected MethodCostTimeAdapter(
                int api, MethodVisitor methodVisitor, int access, String name, String descriptor, String owner, Boolean hasInjectCode
        ) {
            super(api, methodVisitor, access, name, descriptor);
            this.owner = owner;
            this.hasInjectCode = hasInjectCode;
        }

        @Override
        protected void onMethodEnter() {
            if (hasInjectCode) {
                return;
            }
            super.visitInsn(LCONST_0);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LSUB);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

//            Label label0 = new Label();
//            mv.visitLabel(label0);
//            mv.visitLineNumber(21, label0);
//            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//            mv.visitLdcInsn("sdasd");
//            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        }

        @Override
        protected void onMethodExit(int opcode) {
            if (hasInjectCode) {
                return;
            }
            super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitInsn(LADD);
            super.visitFieldInsn(PUTSTATIC, owner, "timer", "J");

            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            // 创建一个 StringBuilder
            super.visitTypeInsn(NEW, "java/lang/StringBuilder");
            super.visitInsn(DUP);
            super.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            // append name
            super.visitLdcInsn(owner + "." + getName() + " cost time:");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                    "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            // append timer value
            super.visitFieldInsn(GETSTATIC, owner, "timer", "J");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;",
                    false);

            super.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }

}