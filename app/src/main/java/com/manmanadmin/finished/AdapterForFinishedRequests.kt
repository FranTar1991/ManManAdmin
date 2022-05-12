package com.manmanadmin.finished

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.ObservableSnapshotArray
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentFinishedRequestItemBinding
import com.manmanadmin.utils.*

class AdapterForFinishedRequests (private val viewModel: FinishedRequestsViewModel,
                                  private val activity: Activity?,
                                  options: FirebaseRecyclerOptions<RequestRemote>,
                                  private val clickListener: OnFinishedRequestClickListener):
    FirebaseRecyclerAdapter<RequestRemote, AdapterForFinishedRequests.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: RequestRemote) {
        holder.bind(model, this, position, activity,   clickListener)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        viewModel.setNumberOfRequests(itemCount)
        val listOfFinishedRequests = getListOfFinishedRequests(snapshots)
        viewModel.setAllRequestsFinished(listOfFinishedRequests)
    }

    private fun getListOfFinishedRequests(snapshots: ObservableSnapshotArray<RequestRemote>): List<RequestRemote> {
        val list = mutableListOf<RequestRemote>()

        for (request in snapshots){
            list.add(request)
        }

        Log.i("MySum","$list")
        return list
    }

    class ViewHolder private constructor(private val binding: FragmentFinishedRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: RequestRemote,
            adapterForFinishedRequests: AdapterForFinishedRequests,
            position: Int,
            activity: Activity?,
            clickListener: OnFinishedRequestClickListener
        ) {
            binding.remoteRequest = item
            binding.clickListener = clickListener



            binding.deletItemImg.setOnClickListener {
                activity?.apply {
                    showAlertDialog(getString(R.string.alert),getString(R.string.want_to_delete),activity,true,null){
                        adapterForFinishedRequests.getRef(position).removeValue()
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

class OnFinishedRequestClickListener(val clickListener: (view: View, item: RequestRemote) -> Unit){
    fun onClick(view: View, requestRemote: RequestRemote) = clickListener(view,requestRemote)
}