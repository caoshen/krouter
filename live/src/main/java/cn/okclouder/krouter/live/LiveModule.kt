package cn.okclouder.krouter.live

import cn.okclouder.krouter.base.IModule

/**
 * @author caoshen
 * @date 2020/10/22
 */
class LiveModule : IModule {
    override fun getRoute(): List<String> {
        val routes = ArrayList<String>()
        routes.add("live-module-music")
        routes.add("live-module-game")
        return routes
    }
}