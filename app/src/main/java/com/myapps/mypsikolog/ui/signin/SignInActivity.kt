package com.myapps.mypsikolog.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.myapps.mypsikolog.databinding.ActivitySignInBinding
import com.myapps.mypsikolog.home.HomeActivity

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

//
//        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
//        val patient = HashMap<String, String>()
//        patient["Address"] = "Jakarta"
//        patient["Age"] = "21"
//        patient["Email"] = "mamang@gmail.com"
//        patient["NameUser"] = "Mamang"
//        patient["Gender"] = "Male"
//        patient["Password"] = "123456"
//        patient["Phone"] = "08756123478"
//
//        database.collection("patients")
//            .add(patient)
//            .addOnSuccessListener {
//                Toast.makeText(this@SignInActivity, "User Inserted", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(this@SignInActivity, "User Inserted", Toast.LENGTH_SHORT).show()
//
//            }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
        }
    }
}