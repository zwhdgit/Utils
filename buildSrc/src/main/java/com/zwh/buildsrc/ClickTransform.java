package com.zwh.buildsrc;

import com.quinn.hunter.transform.HunterTransform;
import com.quinn.hunter.transform.RunVariant;
import com.quinn.hunter.transform.asm.BaseWeaver;

import org.gradle.api.Project;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class ClickTransform extends HunterTransform {

    public Project project;

    public ClickTransform(Project project) {
        super(project);
        this.project = project;
        //依情况而定，看看你需不需要有插件扩展
//        project.getExtensions().create("okHttpHunterExt", OkHttpHunterExtension.class);
        //必须的一步，继承BaseWeaver，帮你隐藏ASM细节
        this.bytecodeWeaver = new ClickWeaver();
    }

    @Override
    protected RunVariant getRunVariant() {
        return RunVariant.ALWAYS;
    }

    //BaseWeaver帮你隐藏了ASM的很多复杂逻辑
    public final class ClickWeaver extends BaseWeaver {

        protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
            return new ClickVisitor(classWriter);
        }

    }

}
