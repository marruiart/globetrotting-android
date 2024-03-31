package com.marina.ruiz.globetrotting.core.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

open class FullScreenDialogFragment(
    private val layout: Int,
    private val positiveBtnResId: Int? = null,
    private val negativeBtnResId: Int? = null,
    private val neutralBtnResId: Int? = null
) : DialogFragment() {
    private var onAcceptFunction: () -> Unit = {}
    private var onCancelFunction: () -> Unit = {}
    private var onRejectFunction: () -> Unit = {}

    fun setOnAcceptFunction(fn: () -> Unit): FullScreenDialogFragment {
        onAcceptFunction = fn
        return this
    }

    fun setOnRejectFunction(fn: () -> Unit): FullScreenDialogFragment {
        onRejectFunction = fn
        return this
    }

    fun setOnCancelFunction(fn: () -> Unit): FullScreenDialogFragment {
        onCancelFunction = fn
        return this
    }

    fun accept() {
        onAcceptFunction()
        super.dismiss()
    }

    fun cancel() {
        onCancelFunction()
        this.dismiss()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val transaction = manager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, this, tag).addToBackStack(null).commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(layout, container, false)
        initListeners(view)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    private fun initListeners(view: View) {
        positiveBtnResId?.let { btn ->
            val acceptButton = view.findViewById<Button>(btn)
            acceptButton.setOnClickListener {
                accept()
            }
        }
        negativeBtnResId?.let { btn ->
            val cancelButton = view.findViewById<Button>(btn)
            cancelButton.setOnClickListener {
                cancel()
            }
        }
        neutralBtnResId?.let { btn ->
            val cancelButton = view.findViewById<Button>(btn)
            cancelButton.setOnClickListener {
                cancel()
            }
        }
    }
}
