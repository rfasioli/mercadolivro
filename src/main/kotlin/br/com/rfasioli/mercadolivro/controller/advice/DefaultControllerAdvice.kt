package br.com.rfasioli.mercadolivro.controller.advice

import br.com.rfasioli.mercadolivro.controller.mapper.toFieldErrorResponse
import br.com.rfasioli.mercadolivro.controller.response.ErrorResponse
import br.com.rfasioli.mercadolivro.exception.BadRequestException
import br.com.rfasioli.mercadolivro.exception.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DefaultControllerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(exception: NotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = exception.message,
                code = exception::class.simpleName
            ),
            HttpStatus.NOT_FOUND
        )

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(exception: BadRequestException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = exception.message,
                code = exception::class.simpleName
            ),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = "Invalid data on request body",
                code = exception::class.simpleName,
                errors = exception.fieldErrors.map { it.toFieldErrorResponse() }
            ),
            HttpStatus.UNPROCESSABLE_ENTITY
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException
    ): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(
                message = exception.localizedMessage,
                code = exception::class.simpleName,
            ),
            HttpStatus.UNPROCESSABLE_ENTITY
        )
}
