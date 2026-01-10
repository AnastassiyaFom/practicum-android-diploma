package ru.practicum.android.diploma.filters.ui.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.ui.presentation.FilterState
import ru.practicum.android.diploma.filters.ui.presentation.FilterViewModel
import ru.practicum.android.diploma.filters.ui.presentation.InputSalaryBoxState
import kotlin.getValue

class FilterFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeInputState().observe(viewLifecycleOwner) {
            renderInputSalaryBoxStyle(it)
        }
        viewModel.observeFilterState().observe(viewLifecycleOwner) {
            renderFilterState(it)
        }

        binding.btnBack.setOnClickListener {
            viewModel.saveFilterParameters()
            findNavController().popBackStack()
        }

        binding.btnSelectPlaceOfWork.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_placeOfWorkFragment)
        }
        binding.btnSelectIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_industryFragment)
        }

        binding.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setOnlyWithSalaryFlag(isChecked)
        }

        binding.btnReset.setOnClickListener {
            viewModel.resetFilters()
            clearSalary()


        }

        binding.btnApply.setOnClickListener {
            viewModel.saveFilterParameters()
            findNavController().popBackStack(R.id.filterFragment, false)
            findNavController().navigate(R.id.action_filterFragment_to_searchFragment)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.btnClearSalary.visibility = View.GONE
                } else {
                    binding.btnClearSalary.visibility = View.VISIBLE
                }
            }
        }
        binding.salaryEditText.addTextChangedListener(textWatcher)

        binding.salaryEditText.setOnFocusChangeListener { v, hasFocus ->
            viewModel.setInputSalaryState(hasFocus, binding.salaryEditText.text.isNullOrEmpty())
        }

        binding.btnClearSalary.setOnClickListener {
            clearSalary()
        }
    }
    private fun clearSalary(){
        binding.salaryEditText.setText("")
        viewModel.setSalary(null)
        viewModel.setInputSalaryState(true,true)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun renderInputSalaryBoxStyle(state: InputSalaryBoxState) {
        var typedArray = requireActivity().theme.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.colorPrimaryVariant))
        val colorPrimaryVariant = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()
        typedArray = requireActivity().theme.obtainStyledAttributes(intArrayOf(R.attr.customEditTextHintColor))
        val customEditTextHintColor = typedArray.getColor(0, Color.GRAY)
        typedArray.recycle()
        typedArray = requireActivity().theme.obtainStyledAttributes(intArrayOf(R.attr.BottomNavEnabled))
        val bottomNavEnabled = typedArray.getColor(0, Color.BLUE)
        typedArray.recycle()
        when (state) {
            is InputSalaryBoxState.EmptyNotFocused -> {
                binding.salaryExpected.setTextColor(customEditTextHintColor)
            }

            is InputSalaryBoxState.NotEmptyNotFocused -> {
                binding.salaryExpected.setTextColor(colorPrimaryVariant)
                viewModel.setSalary(binding.salaryEditText.text.toString().toInt())
            }

            is InputSalaryBoxState.EmptyFocused -> {
                binding.salaryExpected.setTextColor(bottomNavEnabled)
            }

            is InputSalaryBoxState.NotEmptyFocused -> {
                binding.salaryExpected.setTextColor(bottomNavEnabled)
                viewModel.setSalary(binding.salaryEditText.text.toString().toInt())
            }
        }
    }

    private fun renderFilterState(state: FilterState) {
        when (state) {
            is FilterState.Empty -> {
                binding.btnApply.visibility = View.GONE
                binding.btnReset.visibility = View.GONE
            }

            else -> {
                binding.btnApply.visibility = View.VISIBLE
                binding.btnReset.visibility = View.VISIBLE
                setPlaceOfWorkField(viewModel.getPlaceOfWork())
                setIndustryField(viewModel.getIndustry())
                setSalary(viewModel.getSalary())
                binding.checkBox.setChecked(viewModel.getOnlyWithSalaryFlag())

            }
        }
    }
    private fun setSalary(item:Int?){
        var text =""
        if (item == null) text =""
        else if (item>=0) text = item.toString()
        binding.salaryEditText.setText(text)
    }
    private fun setPlaceOfWorkField(item: String?) {
        when (item) {
            null, "" -> {
                binding.placeOfWorkUnselected.visibility = View.VISIBLE
                binding.placeOfWorkSelected.visibility = View.GONE
            }

            else -> {
                binding.placeOfWorkUnselected.visibility = View.GONE
                binding.placeOfWorkSelected.visibility = View.VISIBLE
            }
        }
    }

    private fun setIndustryField(item: String?) {
        when (item) {
            null, "" -> {
                binding.industryUnselected.visibility = View.VISIBLE
                binding.industrySelected.visibility = View.GONE
            }

            else -> {
                binding.industryUnselected.visibility = View.GONE
                binding.industrySelected.visibility = View.VISIBLE
            }

        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        if (!binding.salaryEditText.text.isNullOrEmpty()) {
            viewModel.setSalary(binding.salaryEditText.text.toString().toInt())
        }
        _binding = null
    }
}
