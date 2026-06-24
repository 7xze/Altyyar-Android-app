package com.altayyar.app.presentation.ui.feature.marketplace.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemMarketplaceServiceBinding
import com.altayyar.app.domain.entity.marketplace.MarketplaceService

class ServiceAdapter(
    private val onServiceClick: (MarketplaceService) -> Unit
) : ListAdapter<MarketplaceService, ServiceAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMarketplaceServiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemMarketplaceServiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: MarketplaceService) {
            binding.serviceTitle.text = service.title
            binding.servicePrice.text = itemView.context.getString(R.string.marketplace_price, service.price.toString())
            binding.serviceSeller.text = service.sellerName
            binding.serviceCategory.text = service.category.displayName
            binding.root.setOnClickListener { onServiceClick(service) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<MarketplaceService>() {
        override fun areItemsTheSame(oldItem: MarketplaceService, newItem: MarketplaceService): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MarketplaceService, newItem: MarketplaceService): Boolean =
            oldItem == newItem
    }
}
