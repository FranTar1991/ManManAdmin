package com.manmanadmin.pending

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.manmanadmin.databinding.ManManRequestItemBinding
import com.manmanadmin.utils.ManManRequest

class PendingRequestsAdapter (private val viewModel: PendingRequestsViewModel, options: FirebaseRecyclerOptions<ManManRequest>, private val clickListener: OnManManRequestClickListener):
    FirebaseRecyclerAdapter<ManManRequest, PendingRequestsAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ManManRequest) {
        holder.bind(model, clickListener)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        viewModel.setNumberOfPendingRequests(itemCount)
    }

    class ViewHolder private constructor(private val binding: ManManRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ManManRequest,
            clickListener: OnManManRequestClickListener
        ) {
            binding.manManRequest = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ManManRequestItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OnManManRequestClickListener(val clickListener: (view: View, item: ManManRequest) -> Unit){
    fun onClick(view: View, transactionItem: ManManRequest) = clickListener(view,transactionItem)
}


