package ru.chatrpg.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONException
import ru.chatrpg.main.viewmodels.LoginOnServer

class MainActivity : AppCompatActivity() {

    val loginOnServer: LoginOnServer = LoginOnServer()

    private lateinit var prefs: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = super.getSharedPreferences("myPrefsForJWT", Context.MODE_PRIVATE)
        token = prefs.getString("token", "") ?: "".toString()
        if (token != "") sendMessagesToUser(token)

        val et_Login: EditText = findViewById(R.id.et_Login)
        val et_Password: EditText = findViewById(R.id.et_Password)

        //Create button login
        val btn_Login: Button = findViewById(R.id.bt_Login)
        btn_Login.setOnClickListener {
            val loginResponse = loginOnServer.loginOnServer(et_Login.text.toString(), et_Password.text.toString())
            prefs = super.getSharedPreferences("myPrefsForJWT", Context.MODE_PRIVATE)
            edit = prefs.edit();
            try {
                val saveToken = loginResponse.token;
                edit.putString("token", saveToken);
                edit.commit();
            } catch (e: JSONException) {
                loginResponse.errorMessage.add(e.message ?: "Some bugs")
            }
            sendMessagesToUser(loginResponse.errorMessage)
        }

        //Create button registration
        val btn_Registration: Button = findViewById(R.id.bt_Registration)
        btn_Registration.setOnClickListener {
            val intent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    fun sendMessagesToUser(messages: MutableList<String>) {
        val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
        for (i in messages) {
            toast.setText(i)
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }

    fun sendMessagesToUser(message: String) {
        val toast = Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT)
        toast.setText(message)
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}