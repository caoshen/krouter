package cn.okclouder.krouter

import cn.okclouder.krouter.account.AccountModule
import cn.okclouder.krouter.live.LiveModule

/**
 * @author caoshen
 * @date 2020/10/21
 */
class ModuleRegister {

    companion object {
        fun getAllRoutes(): List<String> {
            val list = ArrayList<String>()
            list.addAll(AccountModule().getRoute())
            list.addAll(LiveModule().getRoute())
            return list
        }
    }
}