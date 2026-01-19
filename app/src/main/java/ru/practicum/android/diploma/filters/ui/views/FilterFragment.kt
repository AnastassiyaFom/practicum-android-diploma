package ru.practicum.android.diploma.filters.ui.views

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.ui.presentation.FilterState
import ru.practicum.android.diploma.filters.ui.presentation.FilterViewModel
import ru.practicum.android.diploma.filters.ui.presentation.InputSalaryBoxState

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
        viewModel.refreshFilters()
        setupSalaryInput()
        setupClickListeners()
        setupNavigate()
        viewModel.observeInputState().observe(viewLifecycleOwner) {
            renderInputSalaryBoxStyle(it)
        }
        viewModel.observeFilterState().observe(viewLifecycleOwner) {
            renderFilterState(it)
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // КОСТЫЛЬ!!!
            viewModel.setInputSalaryState(false, binding.salaryEditText.text.isNullOrEmpty())
            viewModel.setOnlyWithSalaryFlag(isChecked)
        }
    }

    private fun setupNavigate() {
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
        binding.btnApply.setOnClickListener {
            viewModel.saveFilterParameters()
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                "filters_changed",
                true
            )
            findNavController().popBackStack()
        }
    }

    private fun setupClickListeners() {
        binding.btnClearPlaceOfWork.setOnClickListener {
            // КОСТЫЛЬ!!!
            viewModel.setInputSalaryState(hasFocus = false, binding.salaryEditText.text.isNullOrEmpty())
            viewModel.resetPlaceOfWork()
            setPlaceOfWorkField("")
        }
        binding.btnClearIndustry.setOnClickListener {
            // КОСТЫЛЬ!!!
            viewModel.setInputSalaryState(false, binding.salaryEditText.text.isNullOrEmpty())
            viewModel.resetIndustry()
            setIndustryField("")
        }

        binding.btnReset.setOnClickListener {
            binding.salaryEditText.setText("")
            setPlaceOfWorkField("")
            setIndustryField("")
            binding.checkBox.setChecked(false)
            viewModel.setInputSalaryState(false, true)
            viewModel.resetFilters()
        }

        binding.btnClearSalary.setOnClickListener {
            binding.salaryEditText.setText("")
            viewModel.setSalary(null)
            viewModel.setInputSalaryState(true, true)
        }
    }

    private fun setupSalaryInput() {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val test = true
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && s.toString().toLong() <= Int.MAX_VALUE) {
                    viewModel.setSalary(s.toString().toInt())
                } else {
                    viewModel.setSalary(null)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val salary = s?.toString() ?: ""
                if (salary.isEmpty()) {
                    binding.btnClearSalary.visibility = View.GONE
                } else {
                    binding.btnClearSalary.visibility = View.VISIBLE
                    // Если число больше, чем может храниться в Int
                    if (salary.toLong() <= Int.MAX_VALUE) {
                        binding.salaryEditText.setTextColor(getBlackColor())
                    } else {
                        binding.salaryEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    }
                    // Костыль!!!
                    binding.btnApply.visibility = View.VISIBLE
                    binding.btnReset.visibility = View.VISIBLE
                }
            }
        }
        binding.salaryEditText.addTextChangedListener(textWatcher)
        binding.salaryEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.setInputSalaryState(hasFocus, binding.salaryEditText.text.isNullOrEmpty())
        }

        binding.salaryEditText.setOnClickListener {
            viewModel.setInputSalaryState(hasFocus = true, binding.salaryEditText.text.isNullOrEmpty())
        }
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun renderInputSalaryBoxStyle(state: InputSalaryBoxState) {
        val customEditTextHintColor = getHintColor()
        val colorPrimaryVariant = getBlackColor()
        val bottomNavEnabled = getBlueColor()
        when (state) {
            is InputSalaryBoxState.EmptyNotFocused -> {
                binding.salaryExpected.setTextColor(customEditTextHintColor)
            }

            is InputSalaryBoxState.NotEmptyNotFocused -> {
                binding.salaryExpected.setTextColor(colorPrimaryVariant)
            }

            is InputSalaryBoxState.EmptyFocused -> {
                binding.salaryExpected.setTextColor(bottomNavEnabled)
            }

            is InputSalaryBoxState.NotEmptyFocused -> {
                binding.salaryExpected.setTextColor(bottomNavEnabled)
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

    private fun setSalary(item: Int?) {
        var text = ""
        if (item == null) {
            text = ""
        } else if (item >= 0) {
            text = item.toString()
        }
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
                binding.placeOfWorkItem.text = item
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
                binding.industryItem.text = item
            }

        }
    }

    private fun getBlackColor(): Int {
        val typedArray =
            requireActivity()
                .theme.obtainStyledAttributes(
                    intArrayOf(com.google.android.material.R.attr.colorPrimaryVariant)
                )
        val color = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()
        return color
    }

    private fun getBlueColor(): Int {
        val typedArray = requireActivity()
            .theme.obtainStyledAttributes(intArrayOf(R.attr.BottomNavEnabled))
        val color = typedArray.getColor(0, Color.BLUE)
        typedArray.recycle()
        return color
    }

    private fun getHintColor(): Int {
        val typedArray = requireActivity()
            .theme.obtainStyledAttributes(intArrayOf(R.attr.customEditTextHintColor))
        val color = typedArray.getColor(0, Color.GRAY)
        typedArray.recycle()
        return color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.saveFilterParameters()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFilters()
    }
}
