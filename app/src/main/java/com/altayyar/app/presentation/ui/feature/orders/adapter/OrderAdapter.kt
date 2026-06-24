package com.altayyar.app.presentation.ui.feature.orders.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemOrderBinding
import com.altayyar.app.domain.entity.marketplace.Order

class OrderAdapter : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.orderId.text = itemView.context.getString(R.string.marketplace_order_id, order.id.take(8))
            binding.orderStatus.text = order.status.displayName
            binding.orderTotal.text = itemView.context.getString(R.string.marketplace_price, order.totalPrice.toString())
            binding.orderDate.text = itemView.context.getString(R.string.marketplace_order_date, java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale("ar")).format(java.util.Date(order.createdAt)))
            binding.itemsCount.text = itemView.context.getString(if (order.items.size == 1) R.string.marketplace_one_item else R.string.marketplace_items_count, order.items.size)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean =
            oldItem == newItem
    }
}
