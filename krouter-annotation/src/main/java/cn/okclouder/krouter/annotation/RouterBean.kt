package cn.okclouder.krouter.annotation

/**
 * @author caoshen
 * @date 2020/10/16
 */
data class RouterBean(val targetClass: Class<*>, val path: String, val group: String)