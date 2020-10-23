package cn.okclouder.krouter.account

import cn.okclouder.krouter.base.IModule

/**
 * @author caoshen
 * @date 2020/10/21
 */
class AccountModule : IModule {
    override fun getRoute(): List<String> {
        val routes = ArrayList<String>()
        routes.add("account-module-login")
        routes.add("account-module-logout")
        return routes
    }
}