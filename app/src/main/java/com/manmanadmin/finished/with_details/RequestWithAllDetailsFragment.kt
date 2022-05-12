package com.manmanadmin.finished.with_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.manmanadmin.databinding.FragmentRequestWithAllDetailsBinding
import com.manmanadmin.utils.setToolbarUpFunction

class RequestWithAllDetailsFragment : Fragment() {

    private val viewModel: RequestWithDetailsViewModel by viewModels()

    private val args: RequestWithAllDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentRequestWithAllDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestWithAllDetailsBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        viewModel.setFinishedRequestWithDetails(args.finishedRequest)

        setToolbarUpFunction(binding.toolBarDetails)

        return  binding.root
    }

}