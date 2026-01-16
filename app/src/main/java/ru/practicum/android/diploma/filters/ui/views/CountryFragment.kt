package ru.practicum.android.diploma.filters.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filters.ui.adapter.CountriesAdapter
import ru.practicum.android.diploma.filters.ui.presentation.CountryState
import ru.practicum.android.diploma.filters.ui.presentation.CountryViewModel

class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountryViewModel by viewModel()

    private val adapter = CountriesAdapter { country ->
        viewModel.saveSelectedCountry(country)
        findNavController().popBackStack()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvCountries.adapter = adapter

        binding.rvCountries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCountries.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.loadCountries()

    }

    private fun render(state: CountryState) {
        when (state) {
            is CountryState.Loading -> {
                binding.progressCountries.isVisible = true
                binding.rvCountries.isVisible = false
                binding.groupCountriesPlaceholder.isVisible = false
            }

            is CountryState.Content -> {
                binding.progressCountries.isVisible = false
                binding.groupCountriesPlaceholder.isVisible = false
                binding.rvCountries.isVisible = true
                adapter.submitList(state.countries)
            }

            is CountryState.Error -> {
                binding.progressCountries.isVisible = false
                binding.rvCountries.isVisible = false
                binding.groupCountriesPlaceholder.isVisible = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
