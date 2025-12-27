package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class SearchFragment : Fragment(R.layout.fragment_search) {

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
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
    }
}
