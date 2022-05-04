package br.com.rfasioli.mercadolivro.exception

class BooksNotAvailableException : BadRequestException {
    companion object {
        const val MESSAGE: String = "Book(s) not available for purchase"
    }

    constructor() : super(MESSAGE)
    constructor(cause: Throwable?) : super(MESSAGE, cause)
}
