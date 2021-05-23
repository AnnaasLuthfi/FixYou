package com.myapps.mypsikolog.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.myapps.mypsikolog.databinding.ActivitySignInBinding
import com.myapps.mypsikolog.home.HomeActivity
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.Constants.Companion.ADDRESS_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.AGE_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.EMAIL_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.GENDER_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_IS_SIGNED_IN
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_USER_ID
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.PASSWORD_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.PHONE_PATIENTS
import com.myapps.mypsikolog.utils.PreferenceManager

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferenceManager = PreferenceManager(applicationContext)

        //Mempertahankan akun di Login
        if (preferenceManager.getBoolean(KEY_IS_SIGNED_IN)) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener {
            if (binding.email.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter Email address", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()){
                Toast.makeText(this, "Enter Valid Email address", Toast.LENGTH_SHORT).show()
            } else if(binding.password.text.trim().isEmpty()) {
                Toast.makeText(this, "Enter Password address", Toast.LENGTH_SHORT).show()
            } else {
                signIn()
            }
        }
    }

    private fun signIn() {
        startActivity(Intent(this@SignInActivity, HomeActivity::class.java))
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        database.collection(KEY_PATIENTS)
            .whereEqualTo(EMAIL_PATIENTS, binding.email.text.toString())
            .whereEqualTo(PASSWORD_PATIENTS, binding.password.text.toString())
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && task.result!!.documents.size > 0) {
                    val documentSnapshot: DocumentSnapshot = task.result!!.documents[0]
                    preferenceManager.putBoolean(KEY_IS_SIGNED_IN, true)
                    preferenceManager.putString(KEY_USER_ID, documentSnapshot.id)
                    preferenceManager.putString(ADDRESS_PATIENTS, documentSnapshot.getString(ADDRESS_PATIENTS).toString())
                    preferenceManager.putString(AGE_PATIENTS, documentSnapshot.getString(AGE_PATIENTS).toString())
                    preferenceManager.putString(EMAIL_PATIENTS, documentSnapshot.getString(EMAIL_PATIENTS).toString())
                    preferenceManager.putString(GENDER_PATIENTS, documentSnapshot.getString(GENDER_PATIENTS).toString())
                    preferenceManager.putString(NAME_PATIENTS, documentSnapshot.getString(NAME_PATIENTS).toString())
                    preferenceManager.putString(PASSWORD_PATIENTS, documentSnapshot.getString(PASSWORD_PATIENTS).toString())
                    preferenceManager.putString(PHONE_PATIENTS, documentSnapshot.getString(PHONE_PATIENTS).toString())

                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Unable to SignIn", Toast.LENGTH_SHORT).show()
                }
            }
    }
}