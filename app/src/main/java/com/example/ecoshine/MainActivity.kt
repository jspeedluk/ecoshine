package com.example.ecoshine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private val fireBaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkButton.visibility = INVISIBLE

        initLogin()

        buttonScan.setOnClickListener {
            val qrScan = IntentIntegrator(this)
            checkButton.visibility = INVISIBLE
            qrScan.setRequestCode(0x0000c0de)
            qrScan.initiateScan()
        }
        buttonArchive.setOnClickListener {
            val intent = Intent(this, DatePickActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initLogin(){
        if(!isLoggedIn())signInFunc()
    }

    private fun isLoggedIn():Boolean{
        return fireBaseAuth.currentUser != null
    }

    private fun signInFunc() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, 2020)
//        fireBaseAuth.signInWithEmailAndPassword("jaihind@dosto.com", "Jaihind")
//            .addOnCompleteListener {
//                if(it.isSuccessful){
//                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_LONG).show()
//                }
//                else {
//                    Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
//                }
//            }
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
            val scannedResult = HashMap<String, String>()
            scannedResult["house_number"] = result.contents
            val date = SimpleDateFormat("dd-MM-yyyy").format(Date())
            db.collection(date)
                .add(scannedResult)
                .addOnSuccessListener { documentReference ->
                    checkButton.visibility = VISIBLE
                    Log.d("suthar", "DocumentSnapshot added with ID: " + documentReference.id + "content: " + scannedResult["house_number"])
                }
                .addOnFailureListener { e ->
                    textViewName.text = "Error adding to Server!\nPlease Scan again"
                    Log.w("suthar", "Error adding document to Firestore", e)
                }
        }
    }
}