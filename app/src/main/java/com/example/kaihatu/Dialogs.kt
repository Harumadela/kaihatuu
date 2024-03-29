package com.example.kaihatu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmDialog(private val message: String,
                    private val okLabel: String,
                    private val okSelectd: () -> Unit,
                    private val cancelLabel: String,
                    private val cancelSelected: () -> Unit)
    : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage(message)
        builder.setPositiveButton(okLabel){ dialog, which ->
            okSelectd()
        }
        builder.setNegativeButton(cancelLabel){ dialog, which ->
            cancelSelected()
        }
        return builder.create()
    }
}