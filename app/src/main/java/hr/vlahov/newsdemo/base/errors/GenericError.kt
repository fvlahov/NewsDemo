package hr.vlahov.newsdemo.base.errors

import androidx.annotation.StringRes

data class GenericError(
    @StringRes override val messageRes: Int,
    override val displayType: BaseError.ErrorDisplayType = BaseError.ErrorDisplayType.SNACKBAR,
) : BaseError