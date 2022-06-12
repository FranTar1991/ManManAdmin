package com.manmanadmin.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.databinding.ManManRequestItemBinding
import com.manmanadmin.pending.PendingRequestsViewModel

class AdapterForRequests(
    private val viewModel: ViewModelForAdapterInterface,
    options: FirebaseRecyclerOptions<ManManRequest>,
    private val clickListener: OnManManRequestClickListener,
    private val reference: DatabaseReference
):
    FirebaseRecyclerAdapter<ManManRequest, AdapterForRequests.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ManManRequest) {
        holder.bind(model, clickListener, viewModel, reference)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        viewModel.setNumberOfRequests(itemCount)
    }

    class ViewHolder private constructor(private val binding: ManManRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ManManRequest,
            clickListener: OnManManRequestClickListener,
            viewModel: ViewModelForAdapterInterface,
            reference: DatabaseReference
        ) {
            binding.manManRequest = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
            binding.commentsContainer.setStartIconOnClickListener {
                item.requestId?.let {
                    viewModel.saveNewComments(binding.commentsEt.text.toString(),reference.child(it) )
                }
            }

            if (viewModel is PendingRequestsViewModel){
                binding.isReviewedImg.setOnClickListener {
                    item.requestId?.let {
                        viewModel.sendRequestToNextStage(reference.child(it))
                    }
                }
            }

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


