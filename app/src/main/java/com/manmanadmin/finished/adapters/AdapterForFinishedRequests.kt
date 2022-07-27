package com.manmanadmin.finished.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentFinishedRequestItemBinding
import com.manmanadmin.finished.FinishedRequestsViewModel
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.showAlertDialog


class AdapterForFinishedRequests2(
    private val context: Context,
    private val activity: Activity?,
    private val viewModel: FinishedRequestsViewModel?,
    private val listener: OnFinishedRequestClickListener2
):
    ListAdapter<RequestRemote,  AdapterForFinishedRequests2.ViewHolder>(TransactionAdapterDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        viewModel?.let { holder.bind(item, viewModel, activity, listener) }
    }


    class ViewHolder private constructor(private val binding: FragmentFinishedRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RequestRemote,
            viewModel: FinishedRequestsViewModel,
            activity: Activity?,
            listener: OnFinishedRequestClickListener2
        ) {
            binding.remoteRequest = item
            binding.clickListener = listener

            binding.deletItemImg.setOnClickListener {
                activity?.apply {
                    showAlertDialog(getString(R.string.alert),getString(R.string.want_to_delete),activity,true,null){
                       viewModel.deleteThisItem(item)
                    }?.show()
                }


            }

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

class TransactionAdapterDiffCallback : DiffUtil.ItemCallback<RequestRemote>() {
    override fun areItemsTheSame(oldItem: RequestRemote, newItem: RequestRemote): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RequestRemote, newItem: RequestRemote): Boolean {

        return oldItem == newItem
    }
}

class OnFinishedRequestClickListener2(val clickListener: (view: View, item: RequestRemote) -> Unit){
    fun onClick(view: View, requestRemote: RequestRemote) = clickListener(view,requestRemote)
}