package github.earth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        //supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Здесь дожна быть проверка на авторизацию
        //Если залогинен
        startMainActivity()
        //иначе
        startLoginActivity()

    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun startLoginActivity() {

        //finish() Обязательно финиш после отправки в другой активити
    }
}