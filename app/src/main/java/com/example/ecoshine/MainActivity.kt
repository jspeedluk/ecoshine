package com.example.ecoshine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonScan.setOnClickListener {
            val qrscan = IntentIntegrator(this)
            qrscan?.setOrientationLocked(false)
            qrscan.setRequestCode(0x0000c0de)
            qrscan.initiateScan()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("suthar", "Request Code: $requestCode, Result Code: $resultCode")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) parseQrResult(result)
    }

    private fun parseQrResult(result: IntentResult) {
        if (result.contents == null) {
            Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            Log.d("suthar", "Result Not Found")
        } else {
            Log.d("suthar", result.contents)
            Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
            textViewName.text = result.contents
            val db = FirebaseFirestore.getInstance()
        }
    }
}
