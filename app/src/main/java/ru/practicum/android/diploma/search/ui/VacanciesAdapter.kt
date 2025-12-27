package ru.practicum.android.diploma.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.search.domain.models.Vacancy

interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class VacanciesAdapter(
    private val vacancies: List<Vacancy>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<VacancyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        return VacancyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacancies[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
        }
    }

    override fun getItemCount() = vacancies.size
}
