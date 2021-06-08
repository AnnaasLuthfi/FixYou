package com.myapps.mypsikolog.ui.signup

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.myapps.mypsikolog.databinding.ActivitySignUpBinding
import com.myapps.mypsikolog.home.HomeActivity
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.Constants.Companion.ADDRESS_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.AGE_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.EMAIL_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.GENDER_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_IS_SIGNED_IN
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.PASSWORD_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.PHONE_PATIENTS
import com.myapps.mypsikolog.utils.PreferenceManager

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var radioButton: RadioButton
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var database: FirebaseDatabase
    private lateinit var request: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferenceManager = PreferenceManager(applicationContext)

        database = FirebaseDatabase.getInstance()
        request = database.getReference("User")

        // mempertahankan SignUp
        if (preferenceManager.getBoolean(KEY_IS_SIGNED_IN)) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        // Button Sign Up
        binding.btnSignUp.setOnClickListener {
            if (binding.nameSignUp.text.trim().isEmpty()){
                Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show()
            } else if (binding.emailSignUp.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter Email address", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailSignUp.text.toString()).matches()){
                Toast.makeText(this, "Enter Valid Email address", Toast.LENGTH_SHORT).show()
            } else if (binding.phoneNumberSignUp.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter Email address", Toast.LENGTH_SHORT).show()
            } else if(binding.passwordSignUp.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter Password address", Toast.LENGTH_SHORT).show()
            } else if(binding.addressSignUp.text.trim().isEmpty()){
                Toast.makeText(this, "Enter Sddress", Toast.LENGTH_SHORT).show()
            } else if(binding.ageSignUp.text.trim().isEmpty()){
                Toast.makeText(this, "Enter Age", Toast.LENGTH_SHORT).show()
            } else {
                signUp()
            }
        }
    }

    private fun signUp() {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val patient = HashMap<String, String>()
        val radioId: Int = binding.radioGroup.checkedRadioButtonId
        radioButton = findViewById(radioId)

        patient[NAME_PATIENTS] = binding.nameSignUp.text.toString()
        patient[EMAIL_PATIENTS] = binding.emailSignUp.text.toString()
        patient[PHONE_PATIENTS] = binding.phoneNumberSignUp.text.toString()
        patient[PASSWORD_PATIENTS] = binding.passwordSignUp.text.toString()
        patient[ADDRESS_PATIENTS] = binding.addressSignUp.text.toString()
        patient[AGE_PATIENTS] = binding.ageSignUp.text.toString()
        patient[GENDER_PATIENTS] = radioButton.text.toString()

        database.collection(KEY_PATIENTS).add(patient)
            .addOnSuccessListener {documentReference ->
                preferenceManager.putBoolean(KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID, documentReference.id)
                preferenceManager.putString(ADDRESS_PATIENTS, binding.addressSignUp.text.toString())
                preferenceManager.putString(AGE_PATIENTS, binding.ageSignUp.text.toString())
                preferenceManager.putString(EMAIL_PATIENTS, binding.emailSignUp.text.toString())
                preferenceManager.putString(GENDER_PATIENTS, radioButton.text.toString())
                preferenceManager.putString(NAME_PATIENTS, binding.nameSignUp.text.toString())
                preferenceManager.putString(PASSWORD_PATIENTS, binding.passwordSignUp.text.toString())
                preferenceManager.putString(PHONE_PATIENTS, binding.phoneNumberSignUp.text.toString())

                //intent
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        request.child(System.currentTimeMillis().toString())
            .setValue(patient)
    }

    fun checkButton(v: View) {
        val radioId: Int = binding.radioGroup.checkedRadioButtonId
        radioButton = findViewById(radioId)
    }

}