package br.com.rfasioli.mercadolivro.exception

class InvalidBookStatusChangeException : RuntimeException {

    companion object {
        const val MESSAGE: String = "Invalid Book Status Change"
    }

    constructor() : super(MESSAGE)
    constructor(cause: Throwable?) : super(MESSAGE, cause)
}
