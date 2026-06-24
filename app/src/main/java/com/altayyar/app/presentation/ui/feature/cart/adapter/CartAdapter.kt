package com.altayyar.app.presentation.ui.feature.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemCartServiceBinding
import com.altayyar.app.domain.entity.marketplace.CartItem
import com.altayyar.app.domain.entity.marketplace.MarketplaceService

data class CartDisplayItem(
    val cartItem: CartItem,
    val service: MarketplaceService
)

class CartAdapter(
    private val onQuantityChanged: (String, Int) -> Unit,
    private val onRemove: (String) -> Unit
) : ListAdapter<CartDisplayItem, CartAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartServiceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemCartServiceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartDisplayItem) {
            binding.serviceTitle.text = item.service.title
            binding.servicePrice.text = itemView.context.getString(
                R.string.marketplace_price,
                (item.service.price * item.cartItem.quantity).toString()
            )
            binding.quantityText.text = item.cartItem.quantity.toString()
            binding.increaseButton.setOnClickListener {
                onQuantityChanged(item.cartItem.serviceId, item.cartItem.quantity + 1)
            }
            binding.decreaseButton.setOnClickListener {
                onQuantityChanged(item.cartItem.serviceId, item.cartItem.quantity - 1)
            }
            binding.removeButton.setOnClickListener { onRemove(item.cartItem.serviceId) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<CartDisplayItem>() {
        override fun areItemsTheSame(oldItem: CartDisplayItem, newItem: CartDisplayItem): Boolean =
            oldItem.cartItem.serviceId == newItem.cartItem.serviceId

        override fun areContentsTheSame(oldItem: CartDisplayItem, newItem: CartDisplayItem): Boolean =
            oldItem == newItem
    }
}
