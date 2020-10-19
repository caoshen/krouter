package cn.okclouder.krouter.live

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.okclouder.krouter.annotation.Router
import cn.okclouder.krouter.api.Krouter

/**
 * @author caoshen
 * @date 2020/10/19
 */
@Router("live/live")
class LiveActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live)
    }

    fun navigateMain(view: View) {
        Krouter.navigation("app/main")
    }
}