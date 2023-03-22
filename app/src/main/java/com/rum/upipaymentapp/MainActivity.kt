package com.rum.upipaymentapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rum.upipaymentapp.ui.theme.UPIPaymentAppTheme
import com.rum.upipaymentapp.utils.toast

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UPIPaymentAppTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(), color = Color.White
                ) {
                    ResetPasswordScreen(
                        onBackArrowPressed = { super.onBackPressed() },
                        onResetButtonClicked = { currentPassword, newPassword, confirmNewPassword ->
                            resetPassword(currentPassword, newPassword, confirmNewPassword)
                        }
                    )

                    /*Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Pay with UPI")
                                },
                                navigationIcon = {
                                    IconButton(onClick = {}) {
                                        Icon(Icons.Filled.Face, "")
                                    }
                                },
                            )
                        },
                        content = { padding ->
                            Column(
                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(76.dp))
                                TextField(
                                    value = "value",
                                    label = { Text(text = "label") },
                                    onValueChange = {}
                                )
                                OutlinedTextField(
                                    value = "value",
                                    label = { Text("Name") },
                                    onValueChange = {}
                                )
                                Button(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .height(52.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = {
                                        payUsingUpi(
                                            "2.0",
                                            "YOUR_UPI_ID",
                                            "Rumit Name",
                                            "Rumit note",
                                        )

                                    }) {
                                    Text(
                                        text = "Click here",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    )*/
                }
            }
        }
    }

    private fun resetPassword(
        currentPassword: String?, newPassword: String?, confirmNewPassword: String?
    ) {
        if (!newPassword.equals(confirmNewPassword, true)) {
            toast(getString(R.string.app_name))
        } else if (currentPassword.equals(newPassword, true)) {
            toast(getString(R.string.app_name))
        } else {
            toast("Going to reset the password with \ncurrentPassword = $currentPassword\nnewPassword = $newPassword\nconfirmNewPassword = $confirmNewPassword")
            payUsingUpi(
                currentPassword!!,
                newPassword!!,
                "",
                ""
            )
        }
    }

    private fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR").build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            resultLauncher.launch(chooser)
        } else {
            Toast.makeText(
                this@MainActivity,
                "No UPI app found, please install one to continue",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (Activity.RESULT_OK == result.resultCode || result.resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    dataList.add(trxt.toString())
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d(
                    "UPI", "onActivityResult: " + "Return data is null"
                ) //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this@MainActivity)) {
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
                Toast.makeText(this@MainActivity, "Transaction successful.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(this@MainActivity, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    this@MainActivity, "Transaction failed.Please try again", Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this@MainActivity,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected && netInfo.isConnectedOrConnecting && netInfo.isAvailable) {
                    return true
                }
            }
            return false
        }
    }
}