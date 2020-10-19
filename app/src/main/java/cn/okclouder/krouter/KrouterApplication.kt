package cn.okclouder.krouter

import android.app.Application
import cn.okclouder.krouter.api.Krouter

/**
 * @author caoshen
 * @date 2020/10/19
 */
class KrouterApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Krouter.init(this)
    }
}