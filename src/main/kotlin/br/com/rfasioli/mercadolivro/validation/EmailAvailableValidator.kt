package br.com.rfasioli.mercadolivro.validation

import br.com.rfasioli.mercadolivro.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailAvailableValidator(
    private val customerService: CustomerService
) : ConstraintValidator<EmailAvailable, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        value?.takeIf { it.isNotBlank() }
            ?.let { customerService.emailAvailable(it) }
            ?: false
}
