package com.example.ecoshine

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_date_pick.*


class DatePickActivity : AppCompatActivity() {

    private val productAdapter by lazy { ProductAdapter() }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_pick)

        //calender
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        initRecyclerView()

        //button click to view datePicker
        buttonPicker.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    //set to textView
                    setTextString(mDay, mMonth + 1, mYear)
                }, year, month, day
            )
            //show dialog
            dpd.show()
        }
        buttonList.setOnClickListener {
            getProductList()
        }
    }

    private fun getProductList() {
        val db = FirebaseFirestore.getInstance()
        db.collection(textDate.text as String)
            .get()
            .addOnSuccessListener { result ->
                val list = ArrayList<ProductModel>()
                for (document in result) {
                    val data = document.toObject(ProductModel::class.java)
                    list.add(data)
                    Log.d("suthar", "${document.id} => ${document.data}, $data")
                }
                textData.text = "Fetched Data From Server\nTotal Houses Recorded: ${list.size}"
                productAdapter.updateProductList(list)
            }
            .addOnFailureListener { exception ->
                Log.d("suthar", "Error getting documents: ", exception)
            }
    }

    private fun initRecyclerView() {
        recyclerArea.adapter = productAdapter
        recyclerArea.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setTextString(d: Int, m: Int, y: Int) {
        val ds: String = if (d < 10) "0$d" else "$d"
        val ms: String = if (m < 10) "0$m" else "$m"
        textDate.text = "$ds-$ms-$y"
    }
}
