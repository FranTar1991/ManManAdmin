package com.manmanadmin.change_business_status

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.databinding.ChangeBusinesStatusDialogBinding
import com.manmanadmin.main_container.MainContainerViewModel


class ChangeBusinessStatusDialog(private val status_option: Array<String>) : DialogFragment() {

    private var binding: ChangeBusinesStatusDialogBinding? = null
    private var spinner: Spinner? = null
    private lateinit var viewModel: ChangeBusinessStatusViewModel
    private var position: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ChangeBusinesStatusDialogBinding.inflate(layoutInflater)


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding?.root)
                .setTitle(R.string.change_status)
                .setPositiveButton(R.string.ok
                ) { _, _ ->
                    position?.let { pos ->
                        viewModel.changeBusinessStatus(status_option[pos])
                    }
                }.setNegativeButton(R.string.cancel){_,_ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        spinner = binding?.spinner


        val reference = FirebaseDatabase.getInstance().reference.child("data").child("status_service").child("status")
        val repo = ChangeBusinessStatusRepo(reference)
        val factory = ChangeBusinessStatusViewModelFactory(repo, requireNotNull(activity).application)
        viewModel = ViewModelProvider(this, factory)[ChangeBusinessStatusViewModel::class.java]
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.executePendingBindings()
        spinner?.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                position = pos
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        viewModel.currentBusinessStatus.observe(viewLifecycleOwner){ currentStatus ->
            currentStatus?.let {
                spinner?.setSelection(status_option.indexOfFirst { it == currentStatus })
                viewModel.setProgressBarVisibility(View.GONE)
            }
        }

        viewModel.businessStatusChanged.observe(viewLifecycleOwner){
            it?.let {
                dialog?.cancel()
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}