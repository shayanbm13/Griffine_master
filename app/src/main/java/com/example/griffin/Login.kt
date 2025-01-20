package com.example.griffin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.security.MessageDigest
import java.util.Locale
import kotlin.random.Random

class Login : AppCompatActivity() {
    fun generateShortHash(input: String): String {
        val length: Int = 7
        val digest = MessageDigest.getInstance("SHA-256")

        val hashBytes = digest.digest(input.toByteArray())

        val base64Hash = Base64.encodeToString(hashBytes, Base64.NO_WRAP)

        return base64Hash.take(length)
    }
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)


        val login_serial = findViewById<EditText>(R.id.login_serial)
        val ok_btn = findViewById<Button>(R.id.ok_btn)
        val login_serial_number = findViewById<TextView>(R.id.login_serial_number)
        val my_number = Random.nextInt(10000, 100000).toString()
        val hashed_number = generateShortHash(my_number)
        login_serial_number.setText(my_number)

        ok_btn.setOnClickListener {
            if (login_serial.text.toString() == "hans5005") {
                val intent = Intent(this, dashboard::class.java)
                startActivity(intent)

                val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()

                finish()
            }else if (hashed_number.equals(login_serial.text.toString(), ignoreCase = true)){
                val intent = Intent(this, dashboard::class.java)
                startActivity(intent)

                val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.apply()
                finish()

            }else{
                Toast.makeText(this, "Wrong Serial please call Griffin", Toast.LENGTH_SHORT).show()
            }
        }

    }
}