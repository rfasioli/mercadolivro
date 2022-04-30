package br.com.rfasioli.mercadolivro.controller.advice

import br.com.rfasioli.mercadolivro.controller.response.ErrorResponse
import br.com.rfasioli.mercadolivro.exception.BadRequestException
import br.com.rfasioli.mercadolivro.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class DefaultControllerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = exception.message,
                code = exception::class.simpleName
            ),
            HttpStatus.NOT_FOUND
        )

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(exception: BadRequestException, request: WebRequest): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = exception.message,
                code = exception::class.simpleName
            ),
            HttpStatus.BAD_REQUEST
        )
}
