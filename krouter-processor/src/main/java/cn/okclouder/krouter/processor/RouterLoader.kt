package cn.okclouder.krouter.processor

import cn.okclouder.krouter.annotation.RouterBean

/**
 * @author caoshen
 * @date 2020/10/16
 */
class RouterLoader {
    companion object {
        val router: HashMap<String, RouterBean> = HashMap()

        init {
            router["app/home"] = RouterBean(RouterProcessor::class, "app/home", "app")
        }
    }
}