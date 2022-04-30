package br.com.rfasioli.mercadolivro.exception

class BookNotFoundException : NotFoundException {

    companion object {
        const val RESOURCE_NAME: String = "Book"
    }

    constructor(code: Int) : super("$RESOURCE_NAME [$code]")
    constructor(code: Int, cause: Throwable?) : super("$RESOURCE_NAME [$code]", cause)
}
