package com.jnu.togetherpet.ui.fragment.common

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jnu.togetherpet.R
import com.jnu.togetherpet.databinding.ToastCustomBinding

object CustomToast {
    fun displayToast(context: Context, message: String): Toast {

        val inflater = LayoutInflater.from(context)
        val binding: ToastCustomBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_custom, null, false)

        binding.toastText.text = message

        return Toast(context).apply {
            duration = Toast.LENGTH_LONG
            view = binding.root
            setGravity(Gravity.BOTTOM, 0, 100)
            show()
        }.also {
            Log.d("CustomToast", "Toast shown with message: $message")
        }
    }
}