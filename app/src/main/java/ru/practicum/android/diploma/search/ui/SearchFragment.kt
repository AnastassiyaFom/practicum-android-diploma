package ru.practicum.android.diploma.search.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R

class SearchFragment : Fragment(R.layout.fragment_search) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterButton = view.findViewById<View>(R.id.btn_filter)
        filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }
    }
}
