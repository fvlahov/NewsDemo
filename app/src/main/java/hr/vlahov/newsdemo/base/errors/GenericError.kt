package hr.vlahov.newsdemo.base.errors

import androidx.annotation.StringRes
import java.util.UUID

data class GenericError(
    @StringRes override val messageRes: Int,
    override val id: String = UUID.randomUUID().toString(),
    override val displayType: BaseError.ErrorDisplayType = BaseError.ErrorDisplayType.SNACKBAR,
) : BaseError