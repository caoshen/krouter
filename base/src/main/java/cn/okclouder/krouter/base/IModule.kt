package cn.okclouder.krouter.base

/**
 * @author caoshen
 * @date 2020/10/21
 */
interface IModule {

    fun getRoute(): List<String>
}