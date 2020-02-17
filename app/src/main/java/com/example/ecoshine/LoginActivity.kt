package com.example.ecoshine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val fireBaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            signInFunc()
        }
    }
    private fun signInFunc() {
        fireBaseAuth.signInWithEmailAndPassword( inputEmail.text.toString(), inputPassword.text.toString() )
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Sign In Successful", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun isLoggedIn():Boolean{
        return fireBaseAuth.currentUser != null
    }
}
