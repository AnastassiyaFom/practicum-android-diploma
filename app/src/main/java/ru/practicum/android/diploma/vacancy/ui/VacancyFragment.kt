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

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_vacancy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBack(view)
        setupShare(view)
        setupFavorite(view)
        setupPhone(view)
        setupEmail(view)
    }

    private fun setupBack(view: View) {
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupShare(view: View) {
        view.findViewById<ImageButton>(R.id.btn_share).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, VACANCY_SHARE_URL)
            }
            startActivity(Intent.createChooser(intent, null))
        }
    }

    private fun setupFavorite(view: View) {
        view.findViewById<ImageButton>(R.id.btn_favorite).setOnClickListener { button ->
            isFavorite = !isFavorite
            (button as ImageButton).setImageResource(
                if (isFavorite) R.drawable.favorites_on else R.drawable.ic_favorites_off
            )
        }
    }

    private fun setupPhone(view: View) {
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        tvPhone.setOnClickListener {
            val phone = tvPhone.text?.toString().orEmpty().trim()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(Intent.createChooser(intent, null))
            }
        }
    }

    private fun setupEmail(view: View) {
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
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
