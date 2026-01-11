package ru.practicum.android.diploma.filters.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.color.MaterialColors
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentPlaceOfWorkBinding
import ru.practicum.android.diploma.filters.ui.presentation.PlaceOfWorkState
import ru.practicum.android.diploma.filters.ui.presentation.PlaceOfWorkViewModel

class PlaceOfWorkFragment : Fragment() {

    private var _binding: FragmentPlaceOfWorkBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaceOfWorkViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlaceOfWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.itemCountry.setOnClickListener {
            findNavController().navigate(R.id.action_placeOfWorkFragment_to_countryFragment)
        }

        binding.itemRegion.setOnClickListener {
            findNavController().navigate(R.id.action_placeOfWorkFragment_to_regionFragment)
        }

        binding.btnClearCountry.setOnClickListener { viewModel.clearCountry() }
        binding.btnClearRegion.setOnClickListener { viewModel.clearRegion() }

        binding.btnSelect.setOnClickListener { findNavController().popBackStack() }

        viewModel.state.observe(viewLifecycleOwner) { render(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }

    private fun render(state: PlaceOfWorkState) {
        with(binding) {
            tvCountryValue.isVisible = state.isCountrySelected
            tvCountryValue.text = state.countryName
            btnClearCountry.isVisible = state.isCountrySelected
            ivCountryArrow.isVisible = !state.isCountrySelected

            if (state.isCountrySelected) {
                tvCountryLabel.setTextAppearance(R.style.TextAppearance_Regular12)
                tvCountryLabel.setTextColor(MaterialColors.getColor(tvCountryLabel, com.google.android.material.R.attr.colorPrimary))
            } else {
                tvCountryLabel.setTextAppearance(R.style.TextAppearance_Regular16)
                tvCountryLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }

            tvRegionValue.isVisible = state.isRegionSelected
            tvRegionValue.text = state.regionName
            btnClearRegion.isVisible = state.isRegionSelected
            ivRegionArrow.isVisible = !state.isRegionSelected

            if (state.isRegionSelected) {
                tvRegionLabel.setTextAppearance(R.style.TextAppearance_Regular12)
                tvRegionLabel.setTextColor(MaterialColors.getColor(tvRegionLabel, com.google.android.material.R.attr.colorPrimary))
            } else {
                tvRegionLabel.setTextAppearance(R.style.TextAppearance_Regular16)
                tvRegionLabel.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }

            btnSelect.isVisible = state.isSelectButtonVisible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
