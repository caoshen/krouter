package cn.okclouder.krouter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.okclouder.krouter.annotation.Router
import cn.okclouder.krouter.api.Krouter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author caoshen
 * @date 2020/10/19
 */
@Router("app/main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val joinToString = ModuleRegister.getAllRoutes().joinToString("\n") { it }
        tv_routes.text = joinToString
    }

    fun navigateLogin(view: View) {
        Krouter.navigation("app/login")
    }

    fun navigateLive(view: View) {
        Krouter.navigation("live/live")
    }
}