package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.filters.domain.models.Region

class RegionsAdapter(
    private val onClick: (Region) -> Unit
) : ListAdapter<Region, RegionsAdapter.RegionViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val binding = ItemRegionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RegionViewHolder(
        private val binding: ItemRegionBinding,
        private val onClick: (Region) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Region) {
            binding.tvRegionName.text = item.name
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Region>() {
            override fun areItemsTheSame(old: Region, new: Region) = old.id == new.id
            override fun areContentsTheSame(old: Region, new: Region) = old == new
        }
    }
}
