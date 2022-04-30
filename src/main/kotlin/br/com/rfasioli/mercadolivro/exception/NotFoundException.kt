package br.com.rfasioli.mercadolivro.exception

abstract class NotFoundException : RuntimeException {

    companion object {
        const val MESSAGE: String = " Not Found"
    }

    constructor(resource: String) : super(resource + MESSAGE)
    constructor(resource: String, cause: Throwable?) : super(resource + MESSAGE, cause)
}
