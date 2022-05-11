package com.manmanadmin.utils

import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.manmanadmin.R
import com.manmanadmin.utils.STATUS.*

import android.annotation.SuppressLint
import android.widget.*
import androidx.core.view.isVisible
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
fun EditText.setThePriceText(price: String?){
    setText(if (price == context.getString(R.string.to_be_defined)){
        isEnabled = true
        ""
    }else{
        isEnabled = false
        price
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


@BindingAdapter("setTheTextColor")
fun TextView.setTheTextColor(status: STATUS?){
    status?.let {
        setTextColor(when(status){
            Received -> context.getColor(R.color.pending_color)
            Progress -> context.getColor(R.color.progress_color)
            Finished -> context.getColor(R.color.finished_color)
            Unknown, Canceled, null ->context.getColor(R.color.gray)
        })
    }
}

@BindingAdapter("setFirstLetterAsText")
fun TextView.setFirstLetterAsText(agentName: String?){
    agentName?.let {
        text = agentName.substring(0,1).uppercase(Locale.getDefault())
    }
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

