package com.rum.upipaymentapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rum.upipaymentapp.ui.theme.UPIPaymentAppTheme
import com.rum.upipaymentapp.utils.isConnectionAvailable
import com.rum.upipaymentapp.utils.toast

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UPIPaymentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(), color = Color.White
                ) {
                    PaymentScreen(
                        onPayNowButtonClicked = { amount, upi, name, note ->
                            payUsingUpi(amount, upi, name, note)
                        })
                }
            }
        }
    }

    private fun payUsingUpi(amount: String?, upiId: String?, name: String?, note: String?) {
        if (!isConnectionAvailable()) {
            toast("Internet connection is not available. Please check and try again")
            return
        }

        val uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name).appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount).appendQueryParameter("cu", "INR").build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            upiChooserResultLauncher.launch(chooser)
        } else {
            toast("No UPI app found, please install one to continue")
        }
    }

    private val upiChooserResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (Activity.RESULT_OK == result.resultCode || result.resultCode == 11) {
                if (data != null) {
                    val transactionResponse = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $transactionResponse")
                    val dataList = ArrayList<String>()
                    dataList.add(transactionResponse.toString())
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.d("UPI", "onActivityResult: " + "Return data is null")
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable()) {
            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr =
                    response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].lowercase() == "Status".lowercase()) {
                        status = equalStr[1].lowercase()
                    } else if (equalStr[0].lowercase() == "ApprovalRefNo".lowercase() || equalStr[0].lowercase() == "txnRef".lowercase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
                toast("Transaction successful.")
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                toast("Payment cancelled by user.")
            } else {
                toast("Transaction failed.Please try again")
            }
        } else {
            toast("Internet connection is not available. Please check and try again")
        }
    }
}