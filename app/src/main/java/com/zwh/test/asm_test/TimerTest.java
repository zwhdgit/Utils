package com.zwh.test.asm_test;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.Opcodes.ASM9;

import android.util.Log;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * 方法计时
 */
public class TimerTest {

    public int add(int a, int b) throws InterruptedException {
//        Log.e("TAG", "add: " );
//        System.out.println("sdasd");
        int c = a + b;
        Random rand = new Random(System.currentTimeMillis());
        int num = rand.nextInt(300);
        Thread.sleep(100 + num);
        return c;
    }

    public int sub(int a, int b) throws InterruptedException {
        int c = a - b;
        Random rand = new Random(System.currentTimeMillis());
        int num = rand.nextInt(400);
        Thread.sleep(100 + num);
        return c;
    }

    public static void main(String[] args) throws IOException {
        String fileName = "TimerTest.class";
        String file = Constant.parentPath + fileName;
        byte[] bytes = FileUtils.readBytes(file);
//        ClassReader cr = new ClassReader(TimerTest.class.getName());
        ClassReader cr = new ClassReader(bytes);

        //2.然后创建ClassWriter对象，
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new MethodCostTimeClassVisitor(ASM9, cw);
        cr.accept(cv, EXPAND_FRAMES);

        FileUtils.writeBytes(file, cw.toByteArray());
        try {
            new TimerTest().add(0, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}