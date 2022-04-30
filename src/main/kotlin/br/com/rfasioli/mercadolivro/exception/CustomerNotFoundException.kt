package br.com.rfasioli.mercadolivro.exception

class CustomerNotFoundException : RuntimeException {

    companion object {
        const val RESOURCE_NAME: String = "Customer"
    }

    constructor(code: Int) : super("${BookNotFoundException.RESOURCE_NAME} [$code]")
    constructor(code: Int, cause: Throwable?) : super("${BookNotFoundException.RESOURCE_NAME} [$code]", cause)
}
