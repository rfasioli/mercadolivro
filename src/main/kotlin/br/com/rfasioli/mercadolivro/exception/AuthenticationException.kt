package br.com.rfasioli.mercadolivro.exception

class AuthenticationException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable?) : super(message, cause)
}
