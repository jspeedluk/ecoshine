package com.example.ecoshine

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_date_pick.*


class DatePickActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_pick)

        //calender
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //button click to view datePicker
        buttonPicker.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    //set to textView
                    setTextString(mDay, mMonth+1, mYear)
                }, year, month, day
            )
            //show dialog
            dpd.show()
        }
        buttonList.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            db.collection(textDate.text as String)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d("suthar", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("suthar", "Error getting documents: ", exception)
                }
        }
    }
    private fun setTextString(d:Int, m:Int, y:Int){
        var ds: String = if ( d < 10 ) "0$d" else "$d"
        var ms : String = if ( m < 10 ) "0$m" else "$m"
        textDate.text = "$ds-$ms-$y"
    }
}