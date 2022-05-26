package br.com.rfasioli.mercadolivro.security

import br.com.rfasioli.mercadolivro.exception.AuthenticationException
import br.com.rfasioli.mercadolivro.service.UserDetailsCustomService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val userDetails: UserDetailsCustomService,
    private val jwtUtil: JwtUtil
) : BasicAuthenticationFilter(authenticationManager) {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        request.getHeader("Authorization")
            .takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")
            ?.let { getAuthentication(it) }
            ?.let { SecurityContextHolder.getContext().authentication = it }

        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String) =
        token.takeIf { jwtUtil.isValidToken(token) }
            ?.let { jwtUtil.getSubject(it) }
            ?.let { userDetails.loadUserByUsername(it) }
            ?.let { UsernamePasswordAuthenticationToken(it.username, null, it.authorities) }
            ?: throw AuthenticationException("Invalid Token")
}
