package ru.practicum.android.diploma.filters.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.filters.domain.models.Industry

class IndustryViewHolder(private val binding: IndustryItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(industry: Industry, isSelected: Boolean) {
        binding.industryName.text = industry.name
        val drawableRes = if (isSelected) {
            R.drawable.radio_button_check
        } else {
            R.drawable.radio_button_uncheck
        }
        binding.radioBtn.setImageResource(drawableRes)
    }
}
