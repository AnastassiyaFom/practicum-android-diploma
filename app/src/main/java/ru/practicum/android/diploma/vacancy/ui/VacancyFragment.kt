package ru.practicum.android.diploma.vacancy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.R

class VacancyFragment : Fragment() {

    companion object {
        const val ARG_VACANCY_ID = "vacancyId"
    }

    private val viewModel: VacancyViewModel by viewModel()

    private var vacancyId: String? = null
    private var vacancyUrl: String? = null

    private var isFavorite = false

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vacancyId = arguments?.getString(ARG_VACANCY_ID)
    }

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


        setupBack()
        setupShare()
        setupFavorite()
        setupPhone()
        setupEmail()
        observeState()

        val id = vacancyId
        if (id != null) {
            viewModel.load(id)
        } else {
            showPlaceholder(
                drawableRes = R.drawable.il_server_error_vacancy,
                message = getString(R.string.server_error)
            )
        }
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyState.Loading -> showLoading()

                is VacancyState.Content -> {
                    vacancyUrl = state.vacancy.url
                    bindDetails(state.vacancy, state.skillsText, state.primaryPhone)
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
        primaryPhone: String?
    ) {
        bindHeader(vacancy)
        bindCompany(vacancy)
        bindWorkInfo(vacancy)
        bindDescription(vacancy)
        bindSkills(skillsText)
        bindContacts(vacancy, primaryPhone)
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

    private fun bindContacts(vacancy: Vacancy, primaryPhone: String?) {
        val email = vacancy.email.orEmpty().trim()
        val phoneComment = vacancy.contactName.orEmpty().trim()

        val hasPhone = !primaryPhone.isNullOrBlank()
        val hasEmail = email.isNotEmpty()
        val hasAnyContacts = hasPhone || hasEmail

        if (!hasAnyContacts) {
            binding.tvContactsTitle.visibility = View.GONE
            binding.tvPhone.visibility = View.GONE
            binding.tvPhoneComment.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            return
        }

        binding.tvContactsTitle.visibility = View.VISIBLE

        if (hasPhone) {
            binding.tvPhone.visibility = View.VISIBLE
            binding.tvPhone.text = primaryPhone

            if (phoneComment.isNotEmpty()) {
                binding.tvPhoneComment.visibility = View.VISIBLE
                binding.tvPhoneComment.text = phoneComment
            } else {
                binding.tvPhoneComment.visibility = View.GONE
            }
        } else {
            binding.tvPhone.visibility = View.GONE
            binding.tvPhoneComment.visibility = View.GONE
        }

        if (hasEmail) {
            binding.tvEmail.visibility = View.VISIBLE
            binding.tvEmail.text = email
        } else {
            binding.tvEmail.visibility = View.GONE
        }
    }

    private fun setupBack() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupShare() {
        binding.btnShare.setOnClickListener {
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

    private fun setupFavorite() {
        binding.btnFavorite.setOnClickListener { button ->
            isFavorite = !isFavorite
            (button as ImageButton).setImageResource(
                if (isFavorite) R.drawable.favorites_on else R.drawable.ic_favorites_off
            )
        }
    }

    private fun setupPhone() {
        binding.tvPhone.setOnClickListener {
            val phone = binding.tvPhone.text?.toString().orEmpty().trim()
            if (phone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phone")
                }
                startActivity(Intent.createChooser(intent, null))
            }
        }
    }

    private fun setupEmail() {
        binding.tvEmail.setOnClickListener {
            val email = binding.tvEmail.text?.toString().orEmpty().trim()
            if (email.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$email")
                }
                startActivity(Intent.createChooser(intent, null))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

