package com.manmanadmin.finished

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.manmanadmin.databinding.FragmentFinishedRequestItemBinding
import com.manmanadmin.databinding.FragmentFinishedRequestsBinding
import com.manmanadmin.databinding.ManManRequestItemBinding
import com.manmanadmin.utils.*

class AdapterForFinishedRequests (private val viewModel: FinishedRequestsViewModel,
                                  options: FirebaseRecyclerOptions<RequestRemote>,
                                  private val clickListener: OnFinishedRequestClickListener):
    FirebaseRecyclerAdapter<RequestRemote, AdapterForFinishedRequests.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RequestRemote) {
        holder.bind(model, clickListener)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        viewModel.setNumberOfPendingRequests(itemCount)
    }

    class ViewHolder private constructor(private val binding: FragmentFinishedRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RequestRemote,
            clickListener: OnFinishedRequestClickListener
        ) {
            binding.remoteRequest = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentFinishedRequestItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OnFinishedRequestClickListener(val clickListener: (view: View, item: RequestRemote) -> Unit){
    fun onClick(view: View, requestRemote: RequestRemote) = clickListener(view,requestRemote)
}