package ru.chatrpg.main.models.requests

data class RegistrationRequest(
    val username: String,
    val email: String,
    val nickname: String,
    val password: String,
    val passwordConfirm: String
)