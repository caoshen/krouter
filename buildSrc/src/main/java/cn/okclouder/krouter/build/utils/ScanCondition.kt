package cn.okclouder.krouter.build.utils

import java.io.File
import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * @author caoshen
 * @date 2020/10/23
 */
interface ScanCondition {

    fun scan(jar: JarFile, scanEntry: JarEntry)

    fun scan(inputFile: File)

    fun scanFileEnd(inputFile: File, destFile: File)

    fun scanJarEnd(inputJar: File, destJar: File)
}