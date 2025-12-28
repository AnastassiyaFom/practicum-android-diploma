package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchInput()
        setupFilterButton()
    }

    private fun setupSearchInput() {
        binding.searchInput.addTextChangedListener { text ->
            val isEmpty = text.isNullOrEmpty()
            binding.drawableEnd.setImageResource(
                if (isEmpty) R.drawable.ic_search else R.drawable.ic_clear
            )
        }

        binding.drawableEnd.setOnClickListener {
            if (!binding.searchInput.text.isNullOrEmpty()) {
                binding.searchInput.text?.clear()
            }
        }
    }

    private fun setupFilterButton() {
        binding.btnFilter.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
