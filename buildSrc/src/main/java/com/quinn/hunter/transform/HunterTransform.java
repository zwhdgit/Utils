package com.quinn.hunter.transform;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.quinn.hunter.transform.asm.BaseWeaver;
import com.quinn.hunter.transform.asm.ClassLoaderHelper;
import com.quinn.hunter.transform.concurrent.Schedulers;
import com.quinn.hunter.transform.concurrent.Worker;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * implementation 'cn.quinnchen.hunter:hunter-transform:1.2.3'
 * 这里想看源码就拷过来了
 */
public class HunterTransform extends Transform {

    private final Logger logger;

    private static final Set<QualifiedContent.Scope> SCOPES = new HashSet<>();

    static {
        SCOPES.add(QualifiedContent.Scope.PROJECT);
        SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS);
        SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES);
    }

    private final Project project;
    protected BaseWeaver bytecodeWeaver;
    private final Worker worker;
    private boolean emptyRun = false;

    public HunterTransform(Project project) {
        this.project = project;
        this.logger = project.getLogger();
        this.worker = Schedulers.IO();
    }

    // 代表 Transform 对于的 task 名称
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    //  指定 Transform 要处理的数据类型，可以作为输入过滤的一种手段。
    //  在 TransformManager 中定义了很多类型：

    //  CONTENT_CLASS // 代表 javac 编译成的 class 文件，可能是 jar 也可能是目录。
    //  CONTENT_JARS
    //  CONTENT_RESOURCES // 表示处理标准的 java 资源
    //  CONTENT_NATIVE_LIBS
    //  CONTENT_DEX
    //  CONTENT_DEX_WITH_RESOURCES
    //  DATA_BINDING_BASE_CLASS_LOG_ARTIFACT

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    // Transform 要操作的内容范围
    // 1.PROJECT 只有项目内容
    // 2.SUB_PROJECTS 只有子项目内容
    // 3.EXTERNAL_LIBRARIES 只有外部库
    // 4.TESTED_CODE 当前变量（包括依赖项）测试的代码
    // 5.PROVIDED_ONLY 本地或者员村依赖项
    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return SCOPES;
    }

    // 是否增量编译
    @Override
    public boolean isIncremental() {
        return true;
    }

    /**
     * @param inputs TransformInput 是指这些输入文件的一个抽象，包含两个部分
     *               DirectoryInput 集合 是指以源代码方式参与项目编译的所有目录结构及其目录下的源代码
     *               JarInput 集合 是指以 jar 包方式参与项目编译的本地 jar 包和远程 jar 包，包含 aar
     */
    @SuppressWarnings("deprecation")
    @Override
    public void transform(Context context,
                          Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider,
                          boolean isIncremental) throws IOException, TransformException, InterruptedException {
        RunVariant runVariant = getRunVariant();
        if ("debug".equals(context.getVariantName())) {
            emptyRun = runVariant == RunVariant.RELEASE || runVariant == RunVariant.NEVER;
        } else if ("release".equals(context.getVariantName())) {
            emptyRun = runVariant == RunVariant.DEBUG || runVariant == RunVariant.NEVER;
        }
        logger.warn(getName() + " isIncremental = " + isIncremental + ", runVariant = "
                + runVariant + ", emptyRun = " + emptyRun + ", inDuplicatedClassSafeMode = " + inDuplicatedClassSafeMode());
        long startTime = System.currentTimeMillis();
        if (!isIncremental) {
            outputProvider.deleteAll();
        }
        URLClassLoader urlClassLoader = ClassLoaderHelper.getClassLoader(inputs, referencedInputs, project);
        this.bytecodeWeaver.setClassLoader(urlClassLoader);
        boolean flagForCleanDexBuilderFolder = false;
        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                Status status = jarInput.getStatus();
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR);
                if (isIncremental && !emptyRun) {
                    switch (status) {
                        case NOTCHANGED:
                            break;
                        case ADDED:
                        case CHANGED:
                            transformJar(jarInput.getFile(), dest, status);
                            break;
                        case REMOVED:
                            if (dest.exists()) {
                                FileUtils.forceDelete(dest);
                            }
                            break;
                    }
                } else {
                    //Forgive me!, Some project will store 3rd-party aar for several copies in dexbuilder folder,unknown issue.
                    if (inDuplicatedClassSafeMode() && !isIncremental && !flagForCleanDexBuilderFolder) {
                        cleanDexBuilderFolder(dest);
                        flagForCleanDexBuilderFolder = true;
                    }
                    transformJar(jarInput.getFile(), dest, status);
                }
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY);
                FileUtils.forceMkdir(dest);
                if (isIncremental && !emptyRun) {
                    String srcDirPath = directoryInput.getFile().getAbsolutePath();
                    String destDirPath = dest.getAbsolutePath();
                    Map<File, Status> fileStatusMap = directoryInput.getChangedFiles();
                    for (Map.Entry<File, Status> changedFile : fileStatusMap.entrySet()) {
                        Status status = changedFile.getValue();
                        File inputFile = changedFile.getKey();
                        String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath);
                        File destFile = new File(destFilePath);
                        switch (status) {
                            case NOTCHANGED:
                                break;
                            case REMOVED:
                                if (destFile.exists()) {
                                    //noinspection ResultOfMethodCallIgnored
                                    destFile.delete();
                                }
                                break;
                            case ADDED:
                            case CHANGED:
                                try {
                                    FileUtils.touch(destFile);
                                } catch (IOException e) {
                                    //maybe mkdirs fail for some strange reason, try again.
                                    final File parent = destFile.getParentFile();
                                    if (parent == null) {
                                        return;
                                    }
                                    FileUtils.forceMkdir(parent);
                                }
                                transformSingleFile(inputFile, destFile, srcDirPath);
                                break;
                        }
                    }
                } else {
                    transformDir(directoryInput.getFile(), dest);
                }

            }

        }

        worker.await();
        long costTime = System.currentTimeMillis() - startTime;
        logger.warn((getName() + " costed " + costTime + "ms"));
    }

    private void transformSingleFile(final File inputFile, final File outputFile, final String srcBaseDir) {
        worker.submit(() -> {
            bytecodeWeaver.weaveSingleClassToFile(inputFile, outputFile, srcBaseDir);
            return null;
        });
    }

    private void transformDir(final File inputDir, final File outputDir) throws IOException {
        if (emptyRun) {
            FileUtils.copyDirectory(inputDir, outputDir);
            return;
        }
        final String inputDirPath = inputDir.getAbsolutePath();
        final String outputDirPath = outputDir.getAbsolutePath();
        if (inputDir.isDirectory()) {
            for (final File file : com.android.utils.FileUtils.getAllFiles(inputDir)) {
                worker.submit(() -> {
                    String filePath = file.getAbsolutePath();
                    File outputFile = new File(filePath.replace(inputDirPath, outputDirPath));
                    bytecodeWeaver.weaveSingleClassToFile(file, outputFile, inputDirPath);
                    return null;
                });
            }
        }
    }

    private void transformJar(final File srcJar, final File destJar, Status status) {
        worker.submit(() -> {
            if (emptyRun) {
                FileUtils.copyFile(srcJar, destJar);
                return null;
            }
            bytecodeWeaver.weaveJar(srcJar, destJar);
            return null;
        });
    }

    private void cleanDexBuilderFolder(File dest) {
        worker.submit(() -> {
            try {
                String dexBuilderDir = replaceLastPart(dest.getAbsolutePath(), getName(), "dexBuilder");
                //intermediates/transforms/dexBuilder/debug
                File file = new File(dexBuilderDir).getParentFile();
                project.getLogger().warn("clean dexBuilder folder = " + file.getAbsolutePath());
                if (file.exists() && file.isDirectory()) {
                    com.android.utils.FileUtils.deleteDirectoryContents(file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private String replaceLastPart(String originString, String replacement, String toreplace) {
        int start = originString.lastIndexOf(replacement);
        StringBuilder builder = new StringBuilder();
        builder.append(originString, 0, start);
        builder.append(toreplace);
        builder.append(originString.substring(start + replacement.length()));
        return builder.toString();
    }

    @Override
    public boolean isCacheable() {
        return true;
    }

    protected RunVariant getRunVariant() {
        return RunVariant.ALWAYS;
    }

    protected boolean inDuplicatedClassSafeMode() {
        return false;
    }
}
