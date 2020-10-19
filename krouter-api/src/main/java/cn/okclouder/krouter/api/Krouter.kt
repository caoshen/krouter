package cn.okclouder.krouter.api

import android.app.Application
import android.content.Intent
import cn.okclouder.krouter.annotation.RouterBean
import java.util.*

/**
 * @author caoshen
 * @date 2020/10/19
 */
object Krouter {

    private const val ROUTER_PACKAGE_NAME: String = "cn.okclouder.krouter"

    private lateinit var application: Application

    private var routerMap = hashMapOf<String, Map<String, RouterBean>>()

    fun init(application: Application) {
        this.application = application
    }

    fun navigation(path: String) {
        val router = getRouter(path) ?: return
        val targetClass = router.targetClass
        val intent = Intent()
        intent.setClass(application, targetClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    private fun getRouter(path: String): RouterBean? {
        val group = path.substring(0, path.indexOf("/"))
        val groupRouter = routerMap[group]
        return if (groupRouter == null) {
            // load from class file
            val routerClass = "$ROUTER_PACKAGE_NAME." +
                    "${group.capitalize(Locale.getDefault())}RouterLoader"
            val kClass = Class.forName(routerClass).kotlin
            val loader = kClass.java.newInstance()
            val routerMapField = kClass.java.getDeclaredField("routerMap")
            routerMapField.isAccessible = true
            val map = routerMapField.get(loader) as Map<String, RouterBean>
            routerMap[group] = map
            map[path]
        } else {
            groupRouter[path]
        }
    }
}