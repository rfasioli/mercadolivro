package br.com.rfasioli.mercadolivro.security

import br.com.rfasioli.mercadolivro.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateToken(id: String): String =
        Jwts.builder()
            .setSubject(id)
            .setExpiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(SignatureAlgorithm.HS512, secret!!.toByteArray())
            .compact()

    fun isValidToken(token: String): Boolean =
        getClaims(token)
            .takeIf { it.subject != null && it.expiration != null && Date().before(it.expiration) }
            ?.let { true }
            ?: false

    fun getClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(secret!!.toByteArray())
                .parseClaimsJws(token)
                .body
        } catch (ex: JwtException) {
            throw AuthenticationException("Invalid token", ex)
        }
    }

    fun getSubject(token: String): String =
        getClaims(token).subject
}
