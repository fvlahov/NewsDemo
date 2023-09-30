package hr.vlahov.newsdemo.base.errors

interface BaseError {
    val displayType: ErrorDisplayType
    val messageRes: Int

    enum class ErrorDisplayType {
        DIALOG, SNACKBAR, NONE
    }
}