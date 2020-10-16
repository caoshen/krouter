package cn.okclouder.krouter.annotation

import kotlin.reflect.KClass

/**
 * @author caoshen
 * @date 2020/10/16
 */
data class RouterBean(val targetClass: KClass<*>, val path: String, val group: String)