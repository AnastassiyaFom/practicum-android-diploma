package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import android.view.ViewGroup
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

        val searchInput = view.findViewById<EditText>(R.id.search_input)
        val drawableEnd = view.findViewById<ImageView>(R.id.drawable_end)
        val filterButton = view.findViewById<View>(R.id.btn_filter)

        searchInput.addTextChangedListener { text ->
            val isEmpty = text.isNullOrEmpty()
            drawableEnd.setImageResource(
                if (isEmpty) R.drawable.ic_search else R.drawable.ic_clear
            )
        }

        drawableEnd.setOnClickListener {
            if (!searchInput.text.isNullOrEmpty()) {
                searchInput.text?.clear()
            }
        }

        filterButton.setOnClickListener {
            binding.btnFilter.setOnClickListener {
                findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
