package br.com.rfasioli.mercadolivro.exception

class CustomerNotFoundException : RuntimeException {

    companion object {
        const val MESSAGE: String = "Customer Not Found"
    }

    constructor() : super(MESSAGE)
    constructor(cause: Throwable?) : super(MESSAGE, cause)
}
