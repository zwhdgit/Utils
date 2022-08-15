package com.zwh.test.asm_test;

public class Asm {
    /**
     * 空有一个 ClassReader 只能获取类的基本信息，比如类名，超类名，接口等信息，无法获取 class 中的字段、方法等详细信息。
     * 因为 class 中的信息是不变的，但是访问者的逻辑可能千奇百怪，所以 ClassReader 采用了访问者模式来让外部访问 class
     * 的详细信息，我们通过添加不同的 ClassVisitor 来访问 class 中的详细信息 。
     * @ ProcessTest.testReader
     */

    /**
     * ClassWriter 可以将 class 信息生成对应的字节数组，无论是新的 class 还是被需改过的 class。
     * 在创建 ClassWriter 对象的时候，要指定一个 flags参数：
     * ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
     *
     * -   `0`：
     *
     * ASM不会自动计算max stacks和max locals，也不会自动计算stack map frames。
     *
     * -   `ClassWriter.COMPUTE_MAXS`：
     *
     * ASM会自动计算max stacks和max locals，但不会自动计算stack map frames。
     *
     * -   `ClassWriter.COMPUTE_FRAMES`（**推荐使用**）：
     *
     * ASM会自动计算max stacks和max locals，也会自动计算stack map frames。
     *
     * 推荐使用的原因是我们修改了字节码后，局部变量表或操作数栈可能也需要做相应的改变，手动计算的逻辑容易出错，不如直接交给 ASM，当然编译的效率会有一定的下降。
     *
     */

    /**
     * MthodVisitor 是利用 ASM 实现 hook 功能的重要一环，可以通过 visitXxxInsn 方法找到你需要查找的方法从而进行 hook。
     */
}
