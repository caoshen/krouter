package cn.okclouder.krouter.build.transform

import cn.okclouder.krouter.build.utils.ScanCondition
import cn.okclouder.krouter.build.utils.createParent
import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import java.io.File

/**
 * Base transform class
 * extend class should override getName
 *
 * @author caoshen
 * @date 2020/10/23
 */
abstract class DemoTransform : Transform() {

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        transformInvocation?.inputs?.forEach { transformInput ->
            transformInput.directoryInputs.forEach { directoryInput ->
                val inputDir = directoryInput.file
                val outputDir = transformInvocation.outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                if (transformInvocation.isIncremental) {
                    directoryInput.changedFiles.forEach { file, status ->
                        when (status) {
                            Status.NOTCHANGED -> {
                            }
                            Status.ADDED,
                            Status.CHANGED -> {
                                // .class files
                                if (!file.isDirectory && file.name.endsWith(SdkConstants.DOT_CLASS)) {
                                    val out = toOutputFile(outputDir, inputDir, file)
                                    transformFile(file, out)
                                }
                            }
                            Status.REMOVED -> {
                                val toOutputFile = toOutputFile(outputDir, inputDir, file)
                                FileUtils.deleteIfExists(toOutputFile)
                            }
                            else -> {
                            }
                        }
                    }
                } else {
                    FileUtils.getAllFiles(inputDir)
                        .filter {
                            it?.name?.endsWith(SdkConstants.DOT_CLASS) == true
                        }
                        .forEach { file ->
                            val toOutputFile = toOutputFile(outputDir, inputDir, file)
                            transformFile(file, toOutputFile)
                        }
                }
            }

            transformInput.jarInputs.forEach { jarInput ->
                val inputJar = jarInput.file
                val outputJar = transformInvocation.outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                if (transformInvocation.isIncremental) {
                    when (jarInput.status) {
                        Status.NOTCHANGED -> {

                        }
                        Status.ADDED,
                        Status.CHANGED -> {
                            transformJar(inputJar, outputJar)
                        }
                        Status.REMOVED -> {
                            FileUtils.delete(outputJar)
                        }
                        else -> {

                        }
                    }
                } else {
                    transformJar(inputJar, outputJar)
                }
            }
        }
    }

    private fun transformJar(inputJar: File, out: File) {
        // TODO add scan file from jar input
        out.createParent()
        FileUtils.copyFile(inputJar, out)
    }

    private fun transformFile(inputFile: File, out: File) {
        // TODO add scan file from directory input
        out.createParent()
        FileUtils.copyFile(inputFile, out)
    }

    /**
     * copy input file from input dir to output dir
     */
    private fun toOutputFile(outputDir: File, inputDir: File, file: File): File {
        return File(outputDir, FileUtils.relativePossiblyNonExistingPath(file, inputDir))
    }

    /**
     * scan files
     */
    abstract fun getScanConditions(): MutableList<ScanCondition>
}