package com.mhw.nestbee

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity

/**
 * Created by yarolegovich on 08.10.2016.
 */


open class ProgressDialogActivity : AppCompatActivity() {

    private var dialog: ProgressDialog? = null

    protected fun showDialog() {
        if (dialog == null || !(dialog?.isShowing ?: false)) {
            dialog = ProgressDialog.show(this, "", "Waiting for server...")
        }
    }

    protected fun hideDialog() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.cancel()
            dialog = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hideDialog()
    }
}