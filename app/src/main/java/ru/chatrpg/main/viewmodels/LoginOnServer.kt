package ru.chatrpg.main.viewmodels

import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.chatrpg.main.models.response.LoginResponse

class LoginOnServer {
    private lateinit var login: String
    private lateinit var  password: String

    fun loginOnServer(username: String, password: String): LoginResponse {
        this.login = username
        this.password = password
        var loginResponse: LoginResponse = LoginResponse()
        try {
            val response = runBlocking {
                loginUserOnServer()
            }
            if (response.status.value == 200) {
                val responseBody: String = runBlocking {
                    reciveResponseBody(response)
                }
                val gson = GsonBuilder().create()
                loginResponse = gson.fromJson(responseBody, LoginResponse::class.java)
            }
            else {
                loginResponse.errorMessage.add(response.status.toString())
            }

        } catch (e: Exception) {
            loginResponse.errorMessage.add(e.message ?:"Some bugs")
        }

        return loginResponse
    }

    suspend fun loginUserOnServer(): HttpResponse {
        val client = HttpClient()
        val response: HttpResponse = client.post(){
            url("https://chatrpg.jelastic.regruhosting.ru/login")
            contentType(ContentType.Application.Json)
            body = "{ \"login\" : \"$login\", \"password\" : \"$password\"}"

        }
        client.close()
        return response
    }

    suspend fun reciveResponseBody(response: HttpResponse) : String {
        return response.receive()
    }
}