package br.unifor.todolist.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.unifor.todolist.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val it = Intent(applicationContext, LoginActivity::class.java)
            startActivity(it)
            finish()
        }, 3000)

    }
}