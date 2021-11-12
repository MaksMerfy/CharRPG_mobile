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
import ru.chatrpg.main.models.requests.RegistrationRequest
import ru.chatrpg.main.viewmodels.RegistrationOnServer

class RegistrationActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val et_Login: EditText = findViewById(R.id.et_Reg_Login)
        val et_Nickname: EditText = findViewById(R.id.et_Reg_Nickname)
        val et_Email: EditText = findViewById(R.id.et_Reg_Email)
        val et_Password: EditText = findViewById(R.id.et_Reg_Password)
        val et_PasswordConfirm: EditText = findViewById(R.id.et_Reg_PasswordConfirm)

        //Create button registration
        val btn_Registration: Button = findViewById(R.id.bt_Reg_Registration)
        btn_Registration.setOnClickListener {
            val registrationRequest = RegistrationRequest(
                et_Login.text.toString(),
                et_Email.text.toString(),
                et_Nickname.text.toString(),
                et_Password.text.toString(),
                et_PasswordConfirm.text.toString()
            )
            val loginResponse = RegistrationOnServer(registrationRequest).registration()
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