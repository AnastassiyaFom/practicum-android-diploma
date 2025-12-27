package ru.practicum.android.diploma.vacancy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R



class VacancyFragment : Fragment() {

    companion object {
        private const val VACANCY_SHARE_URL = "https://example.com/vacancy"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_vacancy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val btnShare = view.findViewById<ImageButton>(R.id.btn_share)
        val btnFavorite = view.findViewById<ImageButton>(R.id.btn_favorite)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, VACANCY_SHARE_URL)
            }
            startActivity(Intent.createChooser(intent, null))
        }

        var isFavorite = false
        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            btnFavorite.setImageResource(
                if (isFavorite) R.drawable.favorites_on else R.drawable.ic_favorites_off
            )
        }


        tvPhone.setOnClickListener {
            val phone = tvPhone.text?.toString().orEmpty().trim()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(Intent.createChooser(intent, null))
            }
        }


        tvEmail.setOnClickListener {
            val email = tvEmail.text?.toString().orEmpty().trim()
            if (email.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                startActivity(Intent.createChooser(intent, null))
            }
        }
    }
}
