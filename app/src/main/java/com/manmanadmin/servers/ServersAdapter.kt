package com.manmanadmin.servers

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.manmanadmin.databinding.FragmentServerItemBinding
import com.manmanadmin.utils.MMServer

class ServersAdapter  (private val viewModel: ServersViewModel,
                       private val activity: Activity?,
                       options: FirebaseRecyclerOptions<MMServer>,
                       private val clickListener: OnServerClickListener):
    FirebaseRecyclerAdapter<MMServer, ServersAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MMServer) {
        holder.bind(model, viewModel,   clickListener)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        viewModel.setNumberOfServers(snapshots.size)
    }

    class ViewHolder private constructor(private val binding: FragmentServerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: MMServer,
            viewModel: ServersViewModel,
            clickListener: OnServerClickListener
        ) {
            binding.server = item
            binding.viewModel = viewModel
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FragmentServerItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class OnServerClickListener(val clickListener: (view: View, item: MMServer) -> Unit){
    fun onClick(view: View, server: MMServer) = clickListener(view,server)
}