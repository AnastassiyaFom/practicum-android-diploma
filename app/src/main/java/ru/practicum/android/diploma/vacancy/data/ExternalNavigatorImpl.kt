package ru.practicum.android.diploma.vacancy.data

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
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

    override fun makeCall(phoneNumber: String, activity: Activity) {
        val callIntent = Intent().apply {
            action = Intent.ACTION_CALL
            data = Uri.parse("tel:$phoneNumber")
        }
        // Проверяем, есть ли у приложения разрешение на совершение звонков
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Если разрешения нет, запрашиваем его
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            context.startActivity(callIntent)
        }
    }

    // Эту константу надо будет перенести в VacancyFragment!!!!!!!!
    companion object {
        private const val REQUEST_CALL_PERMISSION = 101
    }
}
