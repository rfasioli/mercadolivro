package br.com.rfasioli.mercadolivro.controller.response

data class PageResponse<T>(
    var itens: List<T>,
    var currentPage: Int,
    var totalItems: Long,
    var totalPages: Int
)
