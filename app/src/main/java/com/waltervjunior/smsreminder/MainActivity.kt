package com.waltervjunior.smsreminder

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.waltervjunior.smsreminder.extension.asString
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import android.R.attr.phoneNumber
import android.telephony.SmsManager
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.SEND_SMS


class MainActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS_REQUEST_RECEIVE_SMS = 130
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initComponent()
    }

    private fun initData() {
        setSupportActionBar(toolbar)
    }

    private fun initComponent() {
        etData.onClick {
            val calendar = Calendar.getInstance()
            val datePick = DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener { _, year, month, dia ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dia)

                val timerPick = TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    etData.setText(calendar.time.asString())

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timerPick.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePick.show()
        }

        btnEnviar.onClick {
            sendSMS2(etTelefone.text.toString(), etMensagem.text.toString())
//            toast(etData.text.toString())
        }
    }

    fun sendSMS(phoneNumber: String, message: String) {
        val sms = SmsManager.getDefault()
        try {
            sms.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (ex: SecurityException) {
            ex.printStackTrace()
            toast("Error")
        }
    }

    fun sendSMS2(phoneNumber: String, message: String) {
        var hasSendSMSPermission = 0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasSendSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS)
            if (hasSendSMSPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.SEND_SMS),
                        PERMISSIONS_REQUEST_RECEIVE_SMS)
            } else if (hasSendSMSPermission == PackageManager.PERMISSION_GRANTED) {
                //val intent = Intent(this@MainActivity, ComposeSMSActivity::class.java)
                //startActivity(intent)
                toast("OK")
                sendSMS(phoneNumber, message)
            }
        } else {
            toast("OK?")
//            val intent = Intent(this@MainActivity, ComposeSMSActivity::class.java)
//            startActivity(intent)
            sendSMS(phoneNumber, message)
        }
    }
}
