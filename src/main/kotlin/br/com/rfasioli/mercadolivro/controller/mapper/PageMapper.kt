package br.com.rfasioli.mercadolivro.controller.mapper

import br.com.rfasioli.mercadolivro.controller.response.PageResponse
import org.springframework.data.domain.Page

fun <T> Page<T>.toPageResponse(): PageResponse<T> =
    PageResponse(
        itens = this.content,
        currentPage = this.number,
        totalItems = this.totalElements,
        totalPages = this.totalPages
    )
