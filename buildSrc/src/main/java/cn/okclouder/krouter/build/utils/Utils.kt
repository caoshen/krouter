package cn.okclouder.krouter.build.utils

import java.io.File

/**
 * @author caoshen
 * @date 2020/10/23
 */
fun File.createParent(): Boolean {
    if (!parentFile.exists()) {
        return parentFile.mkdirs()
    }
    return true
}