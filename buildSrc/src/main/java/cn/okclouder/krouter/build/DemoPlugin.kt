package cn.okclouder.krouter.build

import cn.okclouder.krouter.build.transform.AutoRegisterTransform
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author caoshen
 * @date 2020/10/26
 */
class DemoPlugin : Plugin<Project> {
    override fun apply(p0: Project) {
        p0.extensions.findByType(AppExtension::class.java)?.run {
            registerTransform(AutoRegisterTransform())
        }
    }
}