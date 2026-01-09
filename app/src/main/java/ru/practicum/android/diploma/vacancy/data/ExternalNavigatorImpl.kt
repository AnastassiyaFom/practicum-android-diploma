package ru.practicum.android.diploma.vacancy.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun share(vacancyUrl: String) {
        val shareAppIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, vacancyUrl)
        }
        val chooserIntent = Intent.createChooser(shareAppIntent, "Share with")
        chooserIntent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(chooserIntent)
    }

    override fun sendEmail(email: String) {
        val textToEmailIntent = Intent().apply {
            action = Intent.ACTION_SENDTO
            setFlags(FLAG_ACTIVITY_NEW_TASK)
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(textToEmailIntent)
    }

    override fun makeCall(phoneNumber: String) {
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phoneNumber")
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(callIntent)
    }
}
