package br.com.rfasioli.mercadolivro.security.request

data class LoginRequest(
    val email: String,
    val password: String
)
