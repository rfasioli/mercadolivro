package br.com.rfasioli.mercadolivro.exception

abstract class NotActiveException : RuntimeException {

    companion object {
        const val MESSAGE: String = " Not Active"
    }

    constructor(resource: String) : super(resource + MESSAGE)
    constructor(resource: String, cause: Throwable?) : super(resource + MESSAGE, cause)
}
