package com.example.poke

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Login : AppCompatActivity() {

    private lateinit var editTextLogin: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var userPrefs: UserPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        editTextLogin = findViewById(R.id.edit_text_name_id)
        editTextPassword = findViewById(R.id.edit_text_password_id)
        buttonLogin = findViewById(R.id.button_enter_id)
        userPrefs = UserPref(this)

        buttonLogin.setOnClickListener {
            handleLogin()
        }
    }


    private fun handleLogin() {
        val login = editTextLogin.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            return
        }

        if (!userPrefs.isUserRegistered()) {
            // Первый вход — сохраняем
            userPrefs.saveUser(User(login, password))
            Toast.makeText(this, "Аккаунт создан! Добро пожаловать!", Toast.LENGTH_SHORT).show()
            goToMain(login)
        } else {
            // Повторный вход — проверяем
            val savedUser = userPrefs.getSavedUser()
            if (savedUser?.login == login && savedUser.password == password) {
                Toast.makeText(this, "Вход выполнен!", Toast.LENGTH_SHORT).show()
                goToMain(login)
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToMain(login: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_LOGIN", login)
        }
        startActivity(intent)
        finish()
    }
}