package com.zwh.buildsrc;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class ClickMethodVisitor extends MethodVisitor implements Opcodes {

    public int isOnclick;
    private boolean noCheckClick;
    public static Object[] bootstrapMethodArguments;
    public static List<String> lambdaNameList = new ArrayList<>();

    public static final int LAMBDA = 888;
    public static final int NO_LAMBDA = 999;

    public ClickMethodVisitor(MethodVisitor methodVisitor) {
        super(ASM7, methodVisitor);
    }

    public ClickMethodVisitor(MethodVisitor methodVisitor, int isOnclick) {
        super(ASM7, methodVisitor);
        this.isOnclick = isOnclick;
    }

    /**
     * 此方法用户访问注解
     *
     * @param descriptor 可以理解为包路径
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("visitAnnotation" + descriptor);
        if (descriptor.equals("Lcom/zwh/asm_test/NoCheckClick;")) {
            noCheckClick = true;
        }
        return super.visitAnnotation(descriptor, visible);
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
            ClickMethodVisitor.bootstrapMethodArguments = bootstrapMethodArguments;
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

    public int count;

    @Override
    public void visitCode() {
        super.visitCode();
        if (!noCheckClick) {
            if (isOnclick == NO_LAMBDA) {
                System.out.println("visitCode:" + count++);
                isOnclick = 0;
                mv.visitVarInsn(ALOAD, 1);
                mv.visitMethodInsn(INVOKESTATIC, "com/zwh/asm_test/ClickCheck", "isValidClick", "()Z", false);
                Label label = new Label();
                mv.visitJumpInsn(IFNE, label);
                mv.visitInsn(RETURN);
                mv.visitLabel(label);
            } else if (isOnclick == LAMBDA) {
                System.out.println("visitCode:" + count++);
                isOnclick = 0;
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "com/zwh/asm_test/ClickCheck", "isValidClick", "()Z", false);
                Label label = new Label();
                mv.visitJumpInsn(IFNE, label);
                mv.visitInsn(RETURN);
                mv.visitLabel(label);
            }
        }
    }
}