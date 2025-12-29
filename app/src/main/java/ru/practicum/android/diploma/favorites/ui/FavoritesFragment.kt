package ru.practicum.android.diploma.favorites.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.OnItemClickListener
import ru.practicum.android.diploma.search.ui.VacanciesAdapter
import ru.practicum.android.diploma.util.debounce

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by inject()
    private var onVacancyClickDebounce: (Vacancy) -> Unit =
        debounce<Vacancy>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) { vacancy ->
            toVacancyDetail(vacancy.id)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> showEmpty()
            is FavoritesState.Error -> showError()
            is FavoritesState.Content -> showContent(state.vacancies)
        }
    }

    private fun showContent(vacancies: List<Vacancy>) {
        binding.errors.visibility = View.GONE
        binding.vacanciesList.adapter = VacanciesAdapter(vacancies, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                onVacancyClickDebounce(vacancies[position])
            }
        })
        binding.vacanciesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showError() {
        showPlaceholder(viewModel.getUnableToGetListDrawable(), viewModel.getUnableToGetListText())
    }

    private fun showEmpty() {
        showPlaceholder(viewModel.getEmptyVacancyListDrawable(), viewModel.getEmptyVacancyListText())
    }

    private fun showPlaceholder(drawable: Drawable?, text: String) {
        binding.vacanciesList.visibility = View.GONE
        binding.errors.apply {
            visibility = View.VISIBLE
            setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            setText(text)
            gravity = Gravity.CENTER
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.vacanciesList.adapter = null
        _binding = null
    }

    private fun toVacancyDetail(itemId: String) {
        findNavController().navigate(
            ru.practicum.android.diploma.R.id.action_favoritesFragment_to_vacancyFragment,
            bundleOf(ARGS_VACANCY_ID to itemId)
        )
    }

    companion object {
        const val ARGS_VACANCY_ID = "id"
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }
}
