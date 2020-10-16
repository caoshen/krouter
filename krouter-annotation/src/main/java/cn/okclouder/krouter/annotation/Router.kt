package cn.okclouder.krouter.annotation

/**
 * @author caoshen
 * @date 2020/10/16
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Router(val path: String) {
}