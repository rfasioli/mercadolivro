package br.com.rfasioli.mercadolivro.security

import br.com.rfasioli.mercadolivro.exception.AuthenticationException
import br.com.rfasioli.mercadolivro.repository.CustomerRepository
import br.com.rfasioli.mercadolivro.security.request.LoginRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val loginRequest = request.inputStream
                .let { jacksonObjectMapper().readValue(it, LoginRequest::class.java) }

            return customerRepository.findByEmail(loginRequest.email)
                ?.let { UsernamePasswordAuthenticationToken(it.id, loginRequest.password) }
                .let { authenticationManager.authenticate(it) }
                ?: throw AuthenticationException("Authentication failed")
        } catch (ex: Exception) {
            throw AuthenticationException("Authentication failed")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        (authResult.principal as UserCustomDetails)
            .let { jwtUtil.generateToken(it.id.toString()) }
            .also { response.addHeader("Authorization", "Bearer $it") }
    }
}
