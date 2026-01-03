package ru.practicum.android.diploma.vacancy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetails
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R

class VacancyFragment : Fragment() {

    companion object {
        private const val ARG_VACANCY_ID = "vacancyId"
    }
    private val viewModel: VacancyViewModel by viewModel()

    private var vacancyId: String? = null
    private var vacancyUrl: String? = null


    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vacancyId = arguments?.getString(ARG_VACANCY_ID)
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

        setupBack(view)
        setupShare(view)
        setupFavorite(view)
        setupPhone(view)
        setupEmail(view)
        observeState(view)

        val id = vacancyId
        if (id != null) {
            viewModel.load(id)
        } else {
            showPlaceholder(
                view = view,
                drawableRes = R.drawable.server_error_vacancy,
                message = getString(R.string.server_error)
            )
        }
    }

    private fun observeState(view: View) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyState.Loading -> showLoading(view)

                is VacancyState.Content -> {
                    vacancyUrl = state.details.url
                    bindDetails(view, state.details)
                    showContent(view)
                }

                is VacancyState.NotFound -> {
                    showPlaceholder(
                        view = view,
                        drawableRes = R.drawable.vacancy_details_not_found,
                        message = getString(R.string.vacancy_not_found)
                    )
                }

                is VacancyState.Error -> {
                    when (state.error) {
                        VacancyError.NO_INTERNET -> {
                            showPlaceholder(
                                view = view,
                                drawableRes = R.drawable.no_internet_vacancy,
                                message = getString(R.string.no_internet)
                            )
                        }

                        VacancyError.SERVER_ERROR,
                        VacancyError.LOAD_ERROR -> {
                            showPlaceholder(
                                view = view,
                                drawableRes = R.drawable.server_error_vacancy,
                                message = getString(R.string.server_error)
                            )
                        }
                    }
                }

            }
        }
    }

    private fun showLoading(view: View) {
        view.findViewById<View>(R.id.detailsScroll).visibility = View.GONE
        view.findViewById<View>(R.id.placeholderVacancy).visibility = View.GONE
        view.findViewById<View>(R.id.progressBarVacancy).visibility = View.VISIBLE
    }

    private fun showContent(view: View) {
        view.findViewById<View>(R.id.progressBarVacancy).visibility = View.GONE
        view.findViewById<View>(R.id.placeholderVacancy).visibility = View.GONE
        view.findViewById<View>(R.id.detailsScroll).visibility = View.VISIBLE
    }

    private fun showPlaceholder(
        view: View,
        drawableRes: Int,
        message: String
    ) {
        view.findViewById<View>(R.id.detailsScroll).visibility = View.GONE
        view.findViewById<View>(R.id.progressBarVacancy).visibility = View.GONE

        val container = view.findViewById<View>(R.id.placeholderVacancy)
        val image = view.findViewById<ImageView>(R.id.ivPlaceholderVacancy)
        val text = view.findViewById<TextView>(R.id.tvPlaceholderVacancy)

        image.setImageResource(drawableRes)
        text.text = message

        container.translationY = 0f
        container.visibility = View.VISIBLE
    }

    private fun bindDetails(view: View, details: VacancyDetails) {
        view.findViewById<TextView>(R.id.tvVacancyTitle).text = details.name

        view.findViewById<TextView>(R.id.tvSalary).text = formatSalary(details)

        view.findViewById<TextView>(R.id.tvCompanyName).text = details.employerName

        val logoView = view.findViewById<ImageView>(R.id.ivCompanyLogo)
        val logoUrl = details.employerLogoUrl.orEmpty().trim()

        if (logoUrl.isNotEmpty()) {
            Glide.with(view)
                .load(logoUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(logoView)
        } else {
            logoView.setImageResource(R.drawable.logo)
        }

        view.findViewById<TextView>(R.id.tvCompanyCity).text = formatAddress(details)

        view.findViewById<TextView>(R.id.tvExperienceValue).text = details.experience.orEmpty()

        val emp = listOf(details.employment, details.schedule)
            .filter { !it.isNullOrBlank() }
            .joinToString(", ")
        view.findViewById<TextView>(R.id.tvEmploymentValue).text = emp

        val html = details.descriptionHtml.orEmpty()
        view.findViewById<TextView>(R.id.tvDescriptionValue).text =
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)

        val skillsTitle = view.findViewById<TextView>(R.id.tvSkillsTitle)
        val skillsValue = view.findViewById<TextView>(R.id.tvSkillsValue)

        val skills = details.skills
        if (skills.isNullOrEmpty()) {
            skillsTitle.visibility = View.GONE
            skillsValue.visibility = View.GONE
        } else {
            skillsTitle.visibility = View.VISIBLE
            skillsValue.visibility = View.VISIBLE
            skillsValue.text = skills.joinToString("\n") { "•   $it" }
        }

        val contactsTitle = view.findViewById<TextView>(R.id.tvContactsTitle)
        val phoneView = view.findViewById<TextView>(R.id.tvPhone)
        val phoneCommentView = view.findViewById<TextView>(R.id.tvPhoneComment)
        val emailView = view.findViewById<TextView>(R.id.tvEmail)

        val phone = details.phones?.firstOrNull().orEmpty().trim()
        val email = details.email.orEmpty().trim()
        val phoneComment = details.contactName.orEmpty().trim()

        val hasPhone = phone.isNotEmpty()
        val hasEmail = email.isNotEmpty()
        val hasPhoneComment = phoneComment.isNotEmpty()

        if (!hasPhone && !hasEmail) {
            contactsTitle.visibility = View.GONE
            phoneView.visibility = View.GONE
            phoneCommentView.visibility = View.GONE
            emailView.visibility = View.GONE
        } else {
            contactsTitle.visibility = View.VISIBLE

            if (hasPhone) {
                phoneView.visibility = View.VISIBLE
                phoneView.text = phone

                if (hasPhoneComment) {
                    phoneCommentView.visibility = View.VISIBLE
                    phoneCommentView.text = phoneComment
                } else {
                    phoneCommentView.visibility = View.GONE
                }
            } else {
                phoneView.visibility = View.GONE
                phoneCommentView.visibility = View.GONE
            }

            if (hasEmail) {
                emailView.visibility = View.VISIBLE
                emailView.text = email
            } else {
                emailView.visibility = View.GONE
            }
        }
    }

    private fun formatAddress(details: VacancyDetails): String {
        val address = details.address.orEmpty().trim()
        if (address.isNotEmpty()) return address
        return details.areaName.orEmpty()
    }

    private fun formatSalary(details: VacancyDetails): String {
        val from = details.salaryFrom
        val to = details.salaryTo
        val currency = details.currency.orEmpty().trim()

        val base = when {
            from != null && to != null -> "от $from до $to"
            from != null -> "от $from"
            to != null -> "до $to"
            else -> getString(R.string.salary_not_specified)
        }

        return if (currency.isNotEmpty() && base != getString(R.string.salary_not_specified)) {
            "$base $currency"
        } else {
            base
        }
    }

    private fun setupBack(view: View) {
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupShare(view: View) {
        view.findViewById<ImageButton>(R.id.btn_share).setOnClickListener {
            val url = vacancyUrl.orEmpty().trim()
            if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, url)
            }
            startActivity(Intent.createChooser(intent, null))
            }
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




