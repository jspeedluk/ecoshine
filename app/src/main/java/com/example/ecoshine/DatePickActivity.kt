package com.example.ecoshine

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_date_pick.*


class DatePickActivity : AppCompatActivity() {

//    recyclerArea.layoutManager = LinearLayoutManager(this)

    private var adapter: ProductFirestoreRecyclerAdapter? = null

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
//            Log.d("vivek","before ettting manager ")
//            recyclerArea.layoutManager = LinearLayoutManager(this)
            val db = FirebaseFirestore.getInstance()
//            val query = db!!.collection(textDate.text as String).orderBy("house_number", Query.Direction.ASCENDING)
//            Log.d("suthar", "inside loop")
//            val options = FirestoreRecyclerOptions.Builder<ProductModel>()
//                .setQuery(query, ProductModel::class.java).setLifecycleOwner(this).build()
//            Log.d("suthar","cool")
//            adapter = ProductFirestoreRecyclerAdapter(options)
//            recyclerArea.adapter = adapter
//            Log.d("vivek","adapter set ")
//            adapter!!.startListening()

            db.collection(textDate.text as String)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val tem = textData.text
                        textData.text = "$tem\n${document.data}"
                        Log.d("suthar", "${document.id} => ${document.data}")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("suthar", "Error getting documents: ", exception)
                }
        }
    }
//    override fun onStart() {
//        super.onStart()
//        adapter!!.startListening()
//    }
//    override fun onStop() {
//        super.onStop()
//
//        if (adapter != null) {
//            adapter!!.stopListening()
//        }
//    }
    private fun setTextString(d:Int, m:Int, y:Int){
        var ds: String = if ( d < 10 ) "0$d" else "$d"
        var ms : String = if ( m < 10 ) "0$m" else "$m"
        textDate.text = "$ds-$ms-$y"
    }

    private inner class ProductViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view) {
        internal fun setProductName(productName: String) {
            val textView = view.findViewById<TextView>(R.id.houseItem)
            textView.text = productName
        }
    }

    private inner class ProductFirestoreRecyclerAdapter internal constructor(options: FirestoreRecyclerOptions<ProductModel>) : FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(options) {
        override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int, productModel: ProductModel) {
            productViewHolder.setProductName(productModel.house_number)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_house, parent, false)
            return ProductViewHolder(view)
        }
    }
}
