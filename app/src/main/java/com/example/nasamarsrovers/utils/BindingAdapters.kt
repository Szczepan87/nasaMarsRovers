package com.example.nasamarsrovers.utils

import android.content.Context
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.nasamarsrovers.R

@BindingAdapter("value")
fun bindValue(numberPicker: NumberPicker, value: Int) {
    numberPicker.value = value
}

@BindingAdapter("earthDate")
fun earthDate(textView: TextView, earthDate: String) {
    val context = textView.context
    textView.text = context.resources.getString(R.string.earth_date, earthDate)
}

@BindingAdapter("sol")
fun sol(textView: TextView, sol: Int) {
    val context: Context = textView.context
    textView.text = context.resources.getString(R.string.sol, sol)
}

@BindingAdapter("visible")
fun progressVisibility(view: View, isLoading: Boolean?) {
    view.visibility = if (isLoading == true) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}