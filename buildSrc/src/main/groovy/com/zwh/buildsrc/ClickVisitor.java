package com.zwh.buildsrc;


import com.android.tools.r8.w.S;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClickVisitor extends ClassVisitor implements Opcodes {

    public ClickVisitor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String methodName, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, methodName, descriptor, signature, exceptions);
        if (isViewOnclickMethod(access, methodName, descriptor)) {
//            StringBuffer sb = new StringBuffer();
//            if (ClickMethodVisitor.bootstrapMethodArguments != null) {
//                sb.append("bootstrapMethodArguments==");
//                for (Object bootstrapMethodArgument : ClickMethodVisitor.bootstrapMethodArguments) {
//                    if (bootstrapMethodArgument instanceof Handle) {
//                        sb.append("\n \nhandle==" + ((Handle) bootstrapMethodArgument).getName());
//                    } else {
//                        sb.append("\n \n" + bootstrapMethodArgument.toString());
//                    }
//                }
//            }
//            System.out.println(sb);
//
//            ClickMethodVisitor.lambdaNameList.remove(methodName);
//            String s = "ClickVisitor————————" +
//                    "access = " + access + ",methodName=" + methodName + ",descriptor=" + descriptor
//                    + ",signature=" + signature + ",exceptions=" + exceptions;
//            System.out.println(s);
            return new ClickMethodVisitor(methodVisitor, ClickMethodVisitor.NO_LAMBDA);
        } else if (isLambdaOnclick(access, methodName, descriptor)) {
//            System.out.println("LAMBDA: access=" + access + ",methodName=" + methodName + ",descriptor=" + descriptor);
            return new ClickMethodVisitor(methodVisitor, ClickMethodVisitor.LAMBDA);
        }
        return new ClickMethodVisitor(methodVisitor);
    }

    private boolean isViewOnclickMethod(int access, String name, String desc) {
        return
                ((access & ACC_PUBLIC) != 0 && (access & ACC_STATIC) == 0 & (access & ACC_ABSTRACT) == 0) &&
                        name.equals("onClick") && desc.equals("(Landroid/view/View;)V");
    }

    /**
     * 没有对 access 进行判断
     * ACC_PRIVATE | ACC_STATIC | ACC_SYNTHETIC
     */
    public boolean isLambdaOnclick(int access, String name, String desc) {
        return ClickMethodVisitor.lambdaNameList.contains(name);
//                && desc.equals("(Landroid/view/View;)V");
    }
}