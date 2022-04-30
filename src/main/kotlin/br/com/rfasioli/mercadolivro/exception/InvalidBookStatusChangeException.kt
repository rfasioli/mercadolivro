package br.com.rfasioli.mercadolivro.exception

class InvalidBookStatusChangeException : BadRequestException {

    companion object {
        const val MESSAGE: String = "Invalid Book Status Change"
    }

    constructor() : super(MESSAGE)
    constructor(cause: Throwable?) : super(MESSAGE, cause)
}
