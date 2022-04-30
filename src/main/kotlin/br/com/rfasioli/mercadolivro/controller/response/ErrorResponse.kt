package br.com.rfasioli.mercadolivro.controller.response

data class ErrorResponse(
    var message: String? = null,
    var code: String? = null,
    var errors: List<FieldErrorResponse>? = null
)
