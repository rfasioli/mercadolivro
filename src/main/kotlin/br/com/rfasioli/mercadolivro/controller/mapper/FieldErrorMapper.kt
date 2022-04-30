package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.response.FieldErrorResponse
import org.springframework.validation.FieldError

fun FieldError.toFieldErrorResponse() =
    FieldErrorResponse(
        message = this.defaultMessage ?: "Invalid",
        field = this.field
    )
