package ru.chatrpg.main.models.response

data class LoginResponse(
    val token: String = "",
    val errorMessage: MutableList<String> = mutableListOf<String>()
)
