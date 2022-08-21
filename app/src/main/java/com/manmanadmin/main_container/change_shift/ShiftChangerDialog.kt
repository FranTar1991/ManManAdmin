package com.manmanadmin.main_container.change_shift

import android.app.AlertDialog
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.manmanadmin.R
import com.manmanadmin.databinding.ChangeShiftStatusDialogBinding
import com.manmanadmin.main_container.ContainerFragment

class ShiftChangerDialog(
    private val shift_options: Array<String>,
    private val sharedPreferences: SharedPreferences,
    private val containerFragment: ContainerFragment
) : DialogFragment() {

    private var binding: ChangeShiftStatusDialogBinding? = null
    private var spinner: Spinner? = null
    private lateinit var viewModel: ShiftChangerDialogViewModel
    private var position: Int? = null

    interface OnShiftChanged{
        fun onNewShiftSelected(shift: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = ChangeShiftStatusDialogBinding.inflate(layoutInflater)


        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding?.root)
                .setTitle(R.string.change_shift)
                .setPositiveButton(
                    R.string.ok
                ) { _, _ ->
                    position?.let { pos ->

                        containerFragment.onNewShiftSelected(shift_options[pos])
                    }
                }.setNegativeButton(R.string.cancel){ _, _ ->
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

        val factory = ShiftChangerViewModelFactory(requireNotNull(activity).application, sharedPreferences)
        viewModel = ViewModelProvider(this, factory)[ShiftChangerDialogViewModel::class.java]
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner
        spinner = binding?.spinner

        Log.i("MyOptions","${shift_options.indexOfFirst { it == viewModel.getCurrentShift() }}")



        spinner?.onItemSelectedListener =  object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                position = pos
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}