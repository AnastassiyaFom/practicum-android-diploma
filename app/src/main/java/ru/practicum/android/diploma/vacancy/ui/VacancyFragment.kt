package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class VacancyFragment : Fragment() {

    companion object {
        const val ARG_VACANCY_ID = "vacancyId"
    }

    private val viewModel: VacancyViewModel by viewModel<VacancyViewModel> {
        val id = arguments?.getString(ARG_VACANCY_ID) ?: ""
        parametersOf(id)
    }
    private val externalNavigator: ExternalNavigator by inject()
    private var vacancyUrl: String? = null
    private var email: String? = null

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnShare.setOnClickListener {
            vacancyUrl?.let { url -> externalNavigator.share(url) }
        }
        binding.tvEmail.setOnClickListener {
            email?.let { emailAddress -> externalNavigator.sendEmail(emailAddress) }
        }

        observeState()

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            when (isFavorite) {
                true -> binding.btnFavorite.setImageResource(R.drawable.favorites_on)
                false -> binding.btnFavorite.setImageResource(R.drawable.ic_favorites_off)
            }
        }

        binding.btnFavorite.setOnClickListener { viewModel.onFavoriteBtnClicked() }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyState.Loading -> showLoading()

                is VacancyState.Content -> {
                    vacancyUrl = state.vacancy.url
                    email = state.vacancy.email?.trim()
                    bindDetails(state.vacancy, state.skillsText, state.phonesWithComments)
                    showContent()
                }

                is VacancyState.NotFound -> {
                    showPlaceholder(
                        drawableRes = R.drawable.ill_vacancy_details_not_found,
                        message = getString(R.string.vacancy_not_found)
                    )
                }

                is VacancyState.Error -> {
                    when (state.error) {
                        VacancyError.NO_INTERNET -> {
                            showPlaceholder(
                                drawableRes = R.drawable.ill_no_internet,
                                message = getString(R.string.no_internet)
                            )
                        }

                        VacancyError.SERVER_ERROR,
                        VacancyError.LOAD_ERROR -> {
                            showPlaceholder(
                                drawableRes = R.drawable.il_server_error_vacancy,
                                message = getString(R.string.server_error)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.detailsScroll.visibility = View.GONE
        binding.placeholderVacancy.visibility = View.GONE
        binding.progressBarVacancy.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.progressBarVacancy.visibility = View.GONE
        binding.placeholderVacancy.visibility = View.GONE
        binding.detailsScroll.visibility = View.VISIBLE
    }

    private fun showPlaceholder(drawableRes: Int, message: String) {
        binding.detailsScroll.visibility = View.GONE
        binding.progressBarVacancy.visibility = View.GONE

        binding.ivPlaceholderVacancy.setImageResource(drawableRes)
        binding.tvPlaceholderVacancy.text = message
        binding.placeholderVacancy.translationY = 0f
        binding.placeholderVacancy.visibility = View.VISIBLE
    }

    private fun bindDetails(
        vacancy: Vacancy,
        skillsText: String?,
        phonesWithComments: List<Pair<String, String?>>
    ) {
        bindHeader(vacancy)
        bindCompany(vacancy)
        bindWorkInfo(vacancy)
        bindDescription(vacancy)
        bindSkills(skillsText)
        bindContacts(vacancy, phonesWithComments)
    }

    private fun bindHeader(vacancy: Vacancy) {
        binding.tvVacancyTitle.text = vacancy.name
        binding.tvSalary.text = vacancy.salaryTitle
    }

    private fun bindCompany(vacancy: Vacancy) {
        binding.tvCompanyName.text = vacancy.employerName
        binding.tvCompanyCity.text = vacancy.fullAddress
            ?.takeIf { it.isNotBlank() }
            ?: vacancy.areaName.orEmpty()

        val logoUrl = vacancy.logoUrl.orEmpty().trim()

        if (logoUrl.isNotEmpty()) {
            Glide.with(this)
                .load(logoUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.ivCompanyLogo)
        } else {
            binding.ivCompanyLogo.setImageResource(R.drawable.logo)
        }
    }

    private fun bindWorkInfo(vacancy: Vacancy) {
        binding.tvExperienceValue.text = vacancy.experience.orEmpty()

        val emp = listOf(vacancy.employment, vacancy.schedule)
            .filter { !it.isNullOrBlank() }
            .joinToString(", ")

        binding.tvEmploymentValue.text = emp
    }

    private fun bindDescription(vacancy: Vacancy) {
        val html = vacancy.description
        binding.tvDescriptionValue.text =
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    private fun bindSkills(skillsText: String?) {
        if (skillsText.isNullOrBlank()) {
            binding.tvSkillsTitle.visibility = View.GONE
            binding.tvSkillsValue.visibility = View.GONE
        } else {
            binding.tvSkillsTitle.visibility = View.VISIBLE
            binding.tvSkillsValue.visibility = View.VISIBLE
            binding.tvSkillsValue.text = skillsText
        }
    }

    private fun bindContacts(vacancy: Vacancy, phonesWithComments: List<Pair<String, String?>>) {
        setupContactVisibility(vacancy, phonesWithComments)
        if (shouldShowContacts(vacancy, phonesWithComments)) {
            populateContactData(vacancy, phonesWithComments)
        }
    }

    private fun setupContactVisibility(vacancy: Vacancy, phonesWithComments: List<Pair<String, String?>>) {
        val hasAnyContacts = shouldShowContacts(vacancy, phonesWithComments)

        binding.tvContactsTitle.visibility = visibility(hasAnyContacts)
        binding.tvContactName.visibility = visibility(!vacancy.contactName.isNullOrEmpty())
        binding.phonesContainer.visibility = visibility(phonesWithComments.isNotEmpty())
        binding.tvEmail.visibility = visibility(!vacancy.email.isNullOrEmpty())
    }

    private fun shouldShowContacts(vacancy: Vacancy, phonesWithComments: List<Pair<String, String?>>): Boolean {
        return phonesWithComments.isNotEmpty() ||
            !vacancy.email.isNullOrEmpty() ||
            !vacancy.contactName.isNullOrEmpty()
    }

    private fun visibility(shouldShow: Boolean) = if (shouldShow) View.VISIBLE else View.GONE

    private fun populateContactData(vacancy: Vacancy, phonesWithComments: List<Pair<String, String?>>) {
        vacancy.contactName?.let { name ->
            binding.tvContactName.text = name
        }

        if (phonesWithComments.isNotEmpty()) {
            bindPhoneList(phonesWithComments)
        }

        vacancy.email?.let { email ->
            binding.tvEmail.text = email
        }
    }

    private fun bindPhoneList(phonesWithComments: List<Pair<String, String?>>) {
        binding.phonesContainer.removeAllViews()
        phonesWithComments.forEach { (phoneNumber, comment) ->
            binding.phonesContainer.addView(createPhoneView(phoneNumber, comment))
        }
    }

    private fun createPhoneView(phoneNumber: String, comment: String?): View {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.phone_item, binding.phonesContainer, false)
            .apply {
                findViewById<TextView>(R.id.tvPhoneItem).apply {
                    text = formatPhoneText(phoneNumber, comment)
                    setOnClickListener { externalNavigator.makeCall(phoneNumber) }
                }
            }
    }

    private fun formatPhoneText(phoneNumber: String, comment: String?): String {
        return if (!comment.isNullOrEmpty()) "$phoneNumber ($comment)" else phoneNumber
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
