package com.anikinkirill.powerfulandroidapp.ui.auth.state

data class RegistrationFields(
    var registration_email: String? = null,
    var registration_username: String? = null,
    var registration_password: String? = null,
    var registration_confirm_password: String? = null
) {

    class RegistrationError() {
        companion object {
            fun mustFillAllFields() = "All fields are required!"
            fun passwordDoNotMatch() = "Password must match!"
            fun none() = "None"
        }
    }

    fun isValidForRegistration() : String {
        if(registration_password != registration_confirm_password) {
            return RegistrationError.passwordDoNotMatch()
        }
        if(registration_email.isNullOrEmpty() || registration_username.isNullOrEmpty() || registration_password.isNullOrEmpty() || registration_confirm_password.isNullOrEmpty()) {
            return RegistrationError.mustFillAllFields()
        }
        return RegistrationError.none()
    }


}