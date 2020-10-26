package cn.okclouder.krouter.build

/**
 * @author caoshen
 * @date 2020/10/26
 */
object ScanConstants {

    /**
     * IModule interfaces
     */
    const val IMODULE = "cn/okclouder/krouter/base/IModule"

    /**
     * auto register class
     */
    const val AUTO_REGISTER = "cn/okclouder/krouter/ModuleRegister.class"

    const val AUTO_REGISTER_WINDOWS = "cn\\okclouder\\krouter\\ModuleRegister.class"

    fun skipAndroidJar(path: String): Boolean =
        !path.contains("com.android.support")
                && !path.contains("/android/m2repository")
}