package com.zwh.buildsrc;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;

class CustomAsmPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        System.out.println("this is CustomAsmPlugin");
//        System.getProperty("groovy.classpath");
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");
        appExtension.registerTransform(new ClickTransform(project), Collections.EMPTY_LIST);
    }
}