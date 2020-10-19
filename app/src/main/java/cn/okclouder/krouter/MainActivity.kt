package cn.okclouder.krouter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.okclouder.krouter.annotation.Router
import cn.okclouder.krouter.api.Krouter

/**
 * @author caoshen
 * @date 2020/10/19
 */
@Router("app/main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navigateLogin(view: View) {
        Krouter.navigation("app/login")
    }

    fun navigateLive(view: View) {
        Krouter.navigation("live/live")
    }
}