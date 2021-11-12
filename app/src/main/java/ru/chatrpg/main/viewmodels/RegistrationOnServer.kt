package ru.chatrpg.main.viewmodels

import com.google.gson.GsonBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.chatrpg.main.models.requests.RegistrationRequest
import ru.chatrpg.main.models.response.LoginResponse

class RegistrationOnServer(val registrationRequest: RegistrationRequest) {
    fun registration(): LoginResponse {
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
        val response: HttpResponse = client.post() {
            url("https://chatrpg.jelastic.regruhosting.ru/registration")
            contentType(ContentType.Application.Json)
            body = GsonBuilder().create().toJson(registrationRequest)

        }
        client.close()
        return response
    }

    suspend fun reciveResponseBody(response: HttpResponse): String {
        return response.receive()
    }
}