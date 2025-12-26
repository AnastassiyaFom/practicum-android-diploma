package ru.practicum.android.diploma.search.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyViewHolder(private val binding: VacancyItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Vacancy) {
        binding.vacancyNameAndArea.text = model.vacancyTitle
        binding.employerName.text = model.employerName
        binding.salary.text = model.salaryTitle
        Glide.with(itemView)
            .load(model.logoUrl?.toUri())
            .placeholder(R.drawable.logo_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPixel(CORNER_RADIUS)))
            .into(binding.logo)
    }

    private fun dpToPixel(dp: Float): Int {
        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val px = dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(px)
    }

    companion object {
        val CORNER_RADIUS = 12f
        fun from(parent: ViewGroup): VacancyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyItemBinding.inflate(inflater, parent, false)
            return VacancyViewHolder(binding)
        }
    }

}
