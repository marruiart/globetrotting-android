package com.marina.ruiz.globetrotting.ui.main.destinations.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.marina.ruiz.globetrotting.R
import com.marina.ruiz.globetrotting.data.repository.model.Destination
import com.marina.ruiz.globetrotting.databinding.ItemDestinationBinding
import com.marina.ruiz.globetrotting.databinding.ItemPopularDestinationsHomeBinding
import java.text.NumberFormat

/**
 * Adapter class for managing and binding destination items to a RecyclerView.
 *
 * @param onShowDetail Callback function to handle showing destination details.
 * @param onBookNow Callback function to handle booking a destination.
 * @param onFavoriteDestination Callback function to handle favoriting a destination.
 * @param onlyPopular Flag indicating whether to show only popular destinations.
 */
class DestinationsListAdapter(
    private val onShowDetail: (destination: Destination) -> Unit,
    private val onBookNow: (destination: Destination) -> Unit,
    private val onFavoriteDestination: (destinationId: String, isFavorite: Boolean) -> Unit,
    private val onlyPopular: Boolean = false
) : ListAdapter<Destination, DestinationsListAdapter.DestinationViewHolder>(DestinationDiffCallBack()) {

    companion object {
        const val TAG = "GLOB_DEBUG DESTINATIONS_LIST_ADAPTER"
    }

    /**
     * ViewHolder class for managing individual destination views.
     *
     * @param binding The ViewBinding for the destination item.
     * @param context The context of the parent view.
     */
    inner class DestinationViewHolder(
        private val binding: ViewBinding, private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a destination to the standard destination view.
         *
         * @param destination The destination to bind to the view.
         */
        fun bindDestination(destination: Destination) {
            binding as ItemDestinationBinding
            if (destination.imageRef != null) {
                binding.ivItemDestinationBackground.setImageResource(destination.imageRef)
            }
            binding.tvItemDestinationName.text = destination.name
            binding.tvItemDestinationShortDescription.text = destination.shortDescription
            binding.tvItemDestinationPrice.text = context.getString(
                R.string.destination_item_price,
                NumberFormat.getCurrencyInstance().format(destination.price)
            )
            binding.mcvItemDestination.setOnClickListener {
                onShowDetail(destination)
            }
            binding.btnItemDestinationBookNow.setOnClickListener {
                onBookNow(destination)
            }

            binding.cbFavoriteToggle.setOnCheckedChangeListener(null)
            binding.cbFavoriteToggle.isChecked = destination.favorite
            binding.cbFavoriteToggle.setOnCheckedChangeListener { _, isChecked ->
                onFavoriteDestination(destination.id, isChecked)
            }
        }

        /**
         * Binds a destination to the popular destination view.
         *
         * @param destination The destination to bind to the view.
         */
        fun bindPopularDestination(destination: Destination) {
            binding as ItemPopularDestinationsHomeBinding
            if (destination.imageRef != null) {
                binding.ivBackgroundHome.setImageResource(destination.imageRef)
            }
            binding.tvPopularDestinationName.text = destination.name
            binding.mcvPopularDestinationItem.setOnClickListener {
                onShowDetail(destination)
            }
        }
    }

    private class DestinationDiffCallBack : DiffUtil.ItemCallback<Destination>() {
        override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean =
            oldItem.equals(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        var binding: ViewBinding =
            ItemDestinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (onlyPopular) {
            binding = ItemPopularDestinationsHomeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        }
        return DestinationViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = getItem(position)
        if (onlyPopular) {
            holder.bindPopularDestination(destination)
        } else {
            holder.bindDestination(destination)
        }
    }
}