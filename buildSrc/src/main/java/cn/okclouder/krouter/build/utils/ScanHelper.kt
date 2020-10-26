package cn.okclouder.krouter.build.utils

import cn.okclouder.krouter.build.ScanConstants
import com.android.SdkConstants
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * @author caoshen
 * @date 2020/10/26
 */
object ScanHelper {

    private fun shouldSkipScan(conditions: List<ScanCondition?>): Boolean {
        conditions.forEach { scanCondition ->
            if (scanCondition != null) {
                return false
            }
        }
        return true
    }

    fun scanFromJarInput(inputJar: File, destJar: File, conditions: List<ScanCondition?>) {
        if (shouldSkipScan(conditions)) {
            return
        }
        if (ScanConstants.skipAndroidJar(inputJar.absolutePath)) {
            val jar = JarFile(inputJar)
            jar.entries().iterator().forEach {
                conditions.forEach { scanCondition ->
                    scanCondition?.scan(jar, it)
                }
            }
            conditions.forEach {
                it?.scanJarEnd(inputJar, destJar)
            }
        }
    }

    fun scanFromDirectoryInput(inputFile: File, destFile: File, conditions: List<ScanCondition?>) {
        if (shouldSkipScan(conditions)) {
            return
        }
        conditions.forEach {
            it?.scan(inputFile)
        }
        conditions.forEach {
            it?.scanFileEnd(inputFile, destFile)
        }
    }

    fun dealJarFile(
        jarFile: File,
        callback: (jarEntry: JarEntry, jos: JarOutputStream) -> Boolean
    ) {
        val jarAbsolutePath = jarFile.absolutePath
        val bakFilePath = jarAbsolutePath.substring(
            0,
            jarAbsolutePath.length - 4
        ) + System.currentTimeMillis() + SdkConstants.DOT_JAR

        val bakFile = File(bakFilePath)
        jarFile.renameTo(bakFile)
        val bakJarFile = JarFile(bakFilePath)

        val jos = JarOutputStream(FileOutputStream(jarFile))
        bakJarFile.entries().iterator().forEach {
            if (!callback(it, jos)) {
                jos.putNextEntry(ZipEntry(it))
            }
        }

        with(jos) {
            flush()
            finish()
            close()
        }
        bakJarFile.close()
        bakFile.delete()
    }
}