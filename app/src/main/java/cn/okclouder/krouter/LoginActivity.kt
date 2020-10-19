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
@Router("app/login")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun navigateMain(view: View) {
        Krouter.navigation("app/main")
    }
}