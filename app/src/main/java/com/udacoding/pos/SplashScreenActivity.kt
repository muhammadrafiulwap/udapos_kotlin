package com.udacoding.pos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.udacoding.pos.ui.login.LoginActivity
import com.udacoding.pos.utils.moveActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        session = SessionManager(this)

        Handler().postDelayed({
            if (session.is_login)
                moveActivity(MainActivity::class.java)
            else
                moveActivity(LoginActivity::class.java)
        }, 3000L)

    }
}