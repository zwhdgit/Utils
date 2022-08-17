package com.zwh.asm_test;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ProcessTest {

    //    private static final String parentPath = "D:\\Code\\Utils\\app\\src\\main\\java\\com\\zwh\\test\\asm_test\\";
    private static final String parentPath = "D:\\Code\\Utils\\app\\build\\intermediates\\javac\\debug\\classes\\com\\zwh\\test\\asm_test\\";
    private static String classPath = parentPath + "ProcessMan.class";
    private static int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;

    /**
     * 空有一个 ClassReader 只能获取类的基本信息，比如类名，超类名，接口等信息，无法获取 class 中的字段、方法等详细信息。
     * 因为 class 中的信息是不变的，但是访问者的逻辑可能千奇百怪，所以 ClassReader 采用了访问者模式来让外部访问 class
     * 的详细信息，我们通过添加不同的 ClassVisitor 来访问 class 中的详细信息 。
     */
    public static void testReader() {
        byte[] bytes = FileUtils.readBytes(classPath);
        ClassReader cr = new ClassReader(bytes);
        String name = cr.getClassName();
        String superName = cr.getSuperName();
        System.out.println("name: " + name + ", superName: " + superName);
    }

    public static void testClassVisitor() {
        byte[] bytes = FileUtils.readBytes(classPath);
        ClassReader cr = new ClassReader(bytes);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9) {

            @Override
            public FieldVisitor visitField(
                    int access, String name, String descriptor, String signature, Object value
            ) {
                System.out.println("field name: " + name + " ,desc: " + descriptor);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(
                    int access, String name, String descriptor, String signature, String[] exceptions
            ) {
                System.out.println("method name: " + name + " ,desc: " + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        /**
         * ClassReader 通过 accept 方法来接受 ClassVisitor，
         * 在 accept 中 ClassReader 会 read class 的各种信息并回调
         * ClassVisitor 的相关方法。
         */
        cr.accept(cv, parsingOptions);
    }

    public static void createClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        // 规定类的访问权限，类名，超类名
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "com/zwh/HelloWorld", null, "java/lang/Object", null);

        // 创建默认的构造方法
        MethodVisitor mv1 = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv1.visitCode();
        mv1.visitVarInsn(Opcodes.ALOAD, 0);
        mv1.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv1.visitInsn(Opcodes.RETURN);
        mv1.visitMaxs(1, 1);
        mv1.visitEnd();

        // 创建 toString 方法
        MethodVisitor mv2 = cw.visitMethod(Opcodes.ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
        mv2.visitCode();
        mv2.visitLdcInsn("This is a HelloWorld object.");
        mv2.visitInsn(Opcodes.ARETURN);
        mv2.visitMaxs(1, 1);
        mv2.visitEnd();
        cw.visitEnd();

        FileUtils.writeBytes(parentPath,
                "HelloWorld.class",
                cw.toByteArray());
    }

}