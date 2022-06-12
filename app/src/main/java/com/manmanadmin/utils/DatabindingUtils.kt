package com.manmanadmin.utils

import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.manmanadmin.R
import com.manmanadmin.utils.STATUS.*

import android.annotation.SuppressLint
import android.widget.*
import androidx.core.view.isVisible
import com.manmanadmin.reviewing.info.ReviewRequestViewModel
import java.text.DateFormat.getTimeInstance
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("myListener")
fun Spinner.myListener(listener: AdapterView.OnItemSelectedListener?){
    listener?.let {
        onItemSelectedListener = listener
    }

}

@BindingAdapter("setMyHint", "isTitle")
fun EditText.setMyHint(types: String?, isTitle: Boolean? = true){

    isTitle?.let {
        if (isTitle) {
            hint =  when(types){
                resources.getString(R.string.transaction_1) -> context.getString(R.string.purchase)
                resources.getString(R.string.transaction_2) -> context.getString(R.string.shipment)
                resources.getString(R.string.transaction_3) -> context.getString(R.string.ride)
                resources.getString(R.string.transaction_4) -> context.getString(R.string.transaction_request)
                else ->{context.getString(R.string.not_recognized)}
            }

        } else {
            hint =  when(types){
                resources.getString(R.string.transaction_1) -> context.getString(R.string.transaction_1_description)
                resources.getString(R.string.transaction_2) -> context.getString(R.string.transaction_2_description)
                resources.getString(R.string.transaction_3)-> context.getString(R.string.transaction_3_description)
                resources.getString(R.string.transaction_4) -> context.getString(R.string.transaction_4_description)
                else ->{context.getString(R.string.not_recognized)}
            }
        }
    }


}


@BindingAdapter("setJourneyString")
fun TextView.setJourneyString(journey: Journey?){

    journey?.let {
        text = resources.getString(R.string.journey_string, journey.userCity, journey.locationBCity)
    }

}

@BindingAdapter("decideWhichIcon")
fun ImageView.decideWhichIcon(type: String?){
    setImageResource( when(type){
        TYPES.Buy.name -> R.drawable.ic_baseline_shopping_cart_24
        TYPES.Ship.name -> R.drawable.ic_baseline_local_shipping_24
        TYPES.Ride.name -> R.drawable.ic_baseline_thumb_up_24
        else -> R.drawable.ic_baseline_question_mark_24

    })
}

@BindingAdapter("setThePriceText")
fun EditText.setThePriceText(price: Double?){
    setText(if (price  == -1.0){
        ""
    }else{
        price.toString()
    })
}


@BindingAdapter("set_status_when_canceled")
fun TextView.setStatusWhenCanceled(status: STATUS?){

    status?.let {
        text = if (status == Canceled){
            context.getString(R.string.canceled_status_label)
        } else {
            context.getString(R.string.in_progress_status_by)
        }
    }

}

@BindingAdapter("setFirstButton")
fun Button.setFirstButton(status: STATUS?){

    status?.let {
        text = when(status){
            Received -> {context.getString(R.string.edit)}
            Progress -> {context.getString(R.string.track)}
            Finished, Canceled -> {context.getString(R.string.repeat)}
            Unknown , null -> ""

        }
    }


}

@BindingAdapter("set_the_status")
fun TextView.setTheStatus(status: STATUS?){

    status?.let {
        text = when(status){
            Received -> {context.getString(R.string.R)}
            Progress -> {context.getString(R.string.P)}
            Finished -> {context.getString(R.string.F)}
            Canceled ->{context.getString(R.string.C)}
            Unknown, null -> ""

        }
    }


}

@BindingAdapter("set_the_title_text")
fun TextView.setTheTitleText(title: String?){
    title?.let {
        text = context.getString(R.string.request_title, title)
    }
}

@BindingAdapter("setTheItemCountText")
fun TextView.setTheItemCountText(itemCount: Int?){
    itemCount?.let {
        text = context.getString(R.string.item_count, itemCount.toString())
    }
}

@BindingAdapter("setTheTextColor")
fun TextView.setTheTextColor(status: String?){
    status?.let {
        setBackgroundColor(when(status){
            context.resources.getStringArray(R.array.busines_status_options)[0] -> context.getColor(R.color.pending_color)
            context.resources.getStringArray(R.array.busines_status_options)[1]-> context.getColor(R.color.progress_color)
            else -> context.getColor(R.color.white)
        })
    }
}

@BindingAdapter("setFirstLetterAsText")
fun TextView.setFirstLetterAsText(agentName: String?){
    agentName?.let {
        text = agentName.substring(0,1).uppercase(Locale.getDefault())
    }
}

@BindingAdapter("setLastTimeUsed")
fun TextView.setLastTimeUsed(lastTimeUsed: Long?){
    lastTimeUsed?.let {
        text = getTimeSinceLastUpdate(lastTimeUsed)
    }
}

fun getTimeSinceLastUpdate(lastTimeUsed: Long?): String? {
    val simpleDate = getTimeInstance()
    return simpleDate.format(lastTimeUsed)
}


@BindingAdapter("setSecondButton")
fun Button.setSecondButton(status: STATUS?){
    status?.let {
        text = when(status){
            Received -> {context.getString(R.string.cancel)}
            Progress -> {context.getString(R.string.chat_agent)}
            Finished, Canceled -> {context.getString(R.string.chat_customer_service)}
            Unknown, null -> ""
        }
    }
}

@BindingAdapter("myClickListener")
fun ImageView.myClickListener(viewModel: ReviewRequestViewModel?){
    viewModel?.let {
       setOnClickListener {
           showAlertDialog(context.getString(R.string.alert),
               context.getString(R.string.want_to_delete),
               context,true
               ,null){
               viewModel.deleteThisRequest()

           }?.show()
       }
    }
}

@BindingAdapter("setTheText")
fun Button.setTheText(status: String?){
    status?.let {
        text = if(status == Canceled.name){
           context.getString(R.string.skip_request)
        }else{
            context.getString(R.string.continue_btn)
        }
    }


}

@BindingAdapter("setTheText")
fun EditText.setTheText(requestRemote: RequestRemote?){
    requestRemote?.status?.let {status ->
       setText(if(status != Canceled.name){
            requestRemote.userPhone.toString()
        }else{
            context.getString(R.string.skip_request)
        })
    }


}


@BindingAdapter("setVisibility")
fun ImageView.setVisibility(status: STATUS?){
    status?.let {
        isVisible = status == Canceled || status == Finished
    }

}

@BindingAdapter("setVisibilityOfName")
fun TextView.setVisibilityOfName(status: STATUS?){
    status?.let {
        isVisible = status != Canceled
    }

}

@BindingAdapter("setTheTextForInternetCheck")
fun TextView.setTheTextForInternetCheck(status: GeneralStatus?){
    status?.let {
        text = when(status){
            GeneralStatus.loading -> context.getString(R.string.checking_internet_connection)
            GeneralStatus.error -> context.getString(R.string.not_connected)
            GeneralStatus.success -> context.getString(R.string.back_online)
            else -> context.getString(R.string.unknown_state)
        }
    }

}

@SuppressLint("SetTextI18n")
@BindingAdapter("showTheDetails")
fun TextView.showTheDetails(requestRemote: RequestRemote?){
    requestRemote?.let {
        text = "Fecha: ${requestRemote.date}"+"\n\n"+
                "Agente: ${requestRemote.agentName}"+"\n\n"+
                "Nombre del cliente: ${requestRemote.userName}"+"\n\n"+
                "# tel del cliente: ${requestRemote.userPhone}"+"\n\n"+
                "Título: ${requestRemote.title}"+"\n\n"+
                "Detalles: ${requestRemote.details}"+"\n\n"+
                "Precio: ${requestRemote.price}"+"\n\n"+
                "Dirección A: Lat ${requestRemote.userAddressLat} Long ${requestRemote.userAddressLong}"+"\n\n"+
                "Referencia de A: ${requestRemote.userAddressReference}"+"\n\n"+
                "Dirección B: Lat ${requestRemote.locationBAddressLat} Long ${requestRemote.locationBAddressLong}"+"\n\n"+
                "Referencia de B: ${requestRemote.locationBAddressReference}"+"\n\n"+
                "ID: ${requestRemote.id}"+"\n"
    }

}

@BindingAdapter("showMoneyEarned")
fun TextView.showMoneyEarned(allRequests: List<RequestRemote>?){
    allRequests?.let {
        val sumInt = getSumOfMoneyEarnedInRequests(allRequests)
        text = context.getString(R.string.sum_of_requests, sumInt.toString())
    }
}



