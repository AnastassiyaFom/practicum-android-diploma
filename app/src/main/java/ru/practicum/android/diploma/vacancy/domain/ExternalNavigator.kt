package ru.practicum.android.diploma.vacancy.domain

import android.app.Activity

interface ExternalNavigator {
    fun share(vacancyUrl: String)
    fun sendEmail(email: String)
    fun makeCall(phoneNumber: String, activity: Activity)
}
