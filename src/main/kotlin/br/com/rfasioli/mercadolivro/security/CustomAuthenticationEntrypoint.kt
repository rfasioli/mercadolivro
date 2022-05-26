package br.com.rfasioli.mercadolivro.security

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import br.com.rfasioli.mercadolivro.controller.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntrypoint(
    private val objectMapper: ObjectMapper
): AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val errorResponse = ErrorResponse(
            message = authException.message,
            code = authException::class.simpleName)

        response.contentType = "application/json"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.outputStream.print(objectMapper.writeValueAsString(errorResponse))
    }
}