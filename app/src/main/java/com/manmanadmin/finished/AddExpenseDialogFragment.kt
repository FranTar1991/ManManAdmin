package com.manmanadmin.finished

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.manmanadmin.R
import com.manmanadmin.databinding.DialogAddExpenseBinding
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.getDate

class AddExpenseDialogFragment(private val viewModel: FinishedRequestsViewModel ) : DialogFragment() {
    private var binding: DialogAddExpenseBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogAddExpenseBinding.inflate(layoutInflater)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(binding?.root)
                .setTitle(R.string.add_expense)
                .setPositiveButton(
                    R.string.ok
                ) { _, _ ->

                    val expense = getExpenseInfoFromViews()
                    viewModel.addTransactionToFirebase(expense)

                }.setNegativeButton(R.string.cancel){ _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun getExpenseInfoFromViews(): RequestRemote {
        val expenseTitle = binding?.titleExpenseEt?.text.toString()
        val details = binding?.detailsExpenseEt?.text.toString()
        val price = binding?.priceExpenseEt?.text?.toString()?.toDouble() ?: 0.0

        return RequestRemote(agentName ="Gasto Agregado", title = expenseTitle, date = getDate(), details = details, price = price * -1, comments = "is expense")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}