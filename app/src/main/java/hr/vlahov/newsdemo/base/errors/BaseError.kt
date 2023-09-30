package hr.vlahov.newsdemo.base.errors

interface BaseError {
    val id: String
    val displayType: ErrorDisplayType
    val messageRes: Int

    enum class ErrorDisplayType {
        DIALOG, SNACKBAR, NONE
    }
}