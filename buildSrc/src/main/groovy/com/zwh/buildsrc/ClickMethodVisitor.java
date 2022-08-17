package com.zwh.buildsrc;


import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ClickMethodVisitor extends MethodVisitor implements Opcodes {

    public boolean isOnclick;
    public static Object[] bootstrapMethodArguments;
    public static List<String> lambdaNameList = new ArrayList<>();

    public ClickMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM7, methodVisitor);
    }

    public ClickMethodVisitor(MethodVisitor methodVisitor, boolean isOnclick) {
        super(ASM7, methodVisitor);
        this.isOnclick = isOnclick;
    }

    /**
     * setOnclick 通过 visitInvokeDynamicInsn 调用编译后的lambda方法
     * 例：methodVisitor.visitInvokeDynamicInsn("onClick", "()Landroid/view/View$OnClickListener;", new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false),
     * new Object[]{Type.getType("(Landroid/view/View;)V"), new Handle(Opcodes.H_INVOKESTATIC, "com/zwh/test/asm_test/AsmTextActivity", "lambda$onCreate$0", "(Landroid/view/View;)V", false), Type.getType("(Landroid/view/View;)V")});
     * 可通过bootstrapMethodArguments获取编译后的lambda方法名,前提是没有人手动起名类似 lambda$onCreate$0
     */
    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        if (name.equals("onClick")) {
            this.bootstrapMethodArguments = bootstrapMethodArguments;
            for (Object bootstrapMethodArgument : bootstrapMethodArguments) {
                if (bootstrapMethodArgument instanceof Handle) {
                    String name1 = ((Handle) bootstrapMethodArgument).getName();
                    lambdaNameList.add(name1);
                    break;
                }
            }
        }
//        System.out.println("bootstrapMethodHandle==" + bootstrapMethodHandle.toString());
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitCode() {
        super.visitCode();
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKESTATIC, monitorName,
//                "shouldDoClick", "(Landroid/view/View;)Z", false);
//        Label label = new Label();
//        mv.visitJumpInsn(IFNE, label);
//        mv.visitInsn(RETURN);
//        mv.visitLabel(label);
        if (isOnclick) {
            System.out.println("visitCode");
            isOnclick = false;
            Label label0 = new Label();
            mv.visitLabel(label0);
            mv.visitLineNumber(21, label0);
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("sdasd");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
    }
}