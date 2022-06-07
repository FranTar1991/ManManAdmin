package com.manmanadmin.servers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.databinding.FragmentServersBinding
import com.manmanadmin.main_container.ContainerFragmentDirections
import com.manmanadmin.utils.MMServer
import com.manmanadmin.utils.WrapContentLinearLayoutManager
import com.manmanadmin.utils.openWhatsAppWithNumber


class ServersFragment : Fragment() {
    private lateinit var binding: FragmentServersBinding
    private lateinit var viewModel: ServersViewModel
    private lateinit var server: MMServer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServersBinding.inflate(inflater,container,false)
        val repo = ServersRepo()
        val factory = ServersViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(ServersViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        val serversRv = binding.allItemsRv
        serversRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }
        val serversQuery = FirebaseDatabase.getInstance()
            .reference
            .child("servers")
        val onServersClickListener = OnServerClickListener{view, server ->
            this.server = server
            val requestReference = FirebaseDatabase.getInstance().reference.child("users")
                .child(server.currentUserId!!).child("requests").child(server.currentRequestId!!)
            viewModel.setRemoteRequestListener(requestReference)

        }


        viewModel.remoteRequest.observe(viewLifecycleOwner){
            it?.let {
                viewModel.setNavigateToTrackingFragment(it)
            }
        }

        viewModel.callWhatsappWithPhoneNumberLiveData.observe(viewLifecycleOwner){
            it?.let {
                openWhatsAppWithNumber(it, context)
                viewModel.setCallWhatsappWithPhoneNumber(null)
            }
        }

        viewModel.navigateToTrackingFragment.observe(viewLifecycleOwner){
            it?.let {
                this.findNavController()
                    .navigate(ContainerFragmentDirections.actionContainerFragmentToTrackingFragment(remoteRequest = it, server = server))
                viewModel.setNavigateToTrackingFragment(null)
            }
        }

        val adapter = setFinishedRequestAdapter(serversQuery,onServersClickListener, viewLifecycleOwner, viewModel)

        serversRv.adapter = adapter


        return binding.root
    }

    private fun setFinishedRequestAdapter(query: Query,
                                          onServerClickListener: OnServerClickListener,
                                          viewLifecycleOwner: LifecycleOwner,
                                          viewModel: ServersViewModel
    ): ServersAdapter {

        val snapshotParser = SnapshotParser<MMServer> { snapshot ->
            val requestId = snapshot.key
            val token = snapshot.child("FCMToken").getValue(String::class.java)
            val associate = snapshot.child("associate").getValue(String::class.java)
            val lastTimeUsed = snapshot.child("lastTimeUsed").getValue(Long::class.java)
            val phoneNumber = snapshot.child("phoneNumber").getValue(String::class.java)
            val serverStatus = snapshot.child("serverStatus").getValue(String::class.java)
            val currentRequestId = snapshot.child("current").child("requestId").getValue(String::class.java)
            val currentUserId = snapshot.child("current").child("userId").getValue(String::class.java)


            MMServer(FCMToken = token, associate = associate, lastTimeUsed =  lastTimeUsed,
                phoneNumber=phoneNumber, serverStatus=serverStatus, currentRequestId=currentRequestId, currentUserId =currentUserId  )
        }

        val options: FirebaseRecyclerOptions<MMServer> = FirebaseRecyclerOptions.Builder<MMServer>()
            .setQuery(query, snapshotParser)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        return ServersAdapter(viewModel, activity,options,  onServerClickListener)

    }

}