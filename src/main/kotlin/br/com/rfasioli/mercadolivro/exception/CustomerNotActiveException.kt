package br.com.rfasioli.mercadolivro.exception

class CustomerNotActiveException : NotActiveException {

    companion object {
        const val RESOURCE_NAME: String = "Customer"
    }

    constructor(code: Int) : super("$RESOURCE_NAME [$code]")
    constructor(code: Int, cause: Throwable?) : super("$RESOURCE_NAME [$code]", cause)
}
