package com.myapps.mypsikolog.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityHomeBinding
import com.myapps.mypsikolog.psycholog.PsychologActivity
import com.myapps.mypsikolog.ui.AfterSplashActivity
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_FCM_TOKEN
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_USER_ID
import com.myapps.mypsikolog.utils.PreferenceManager

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHome.toolbar)
        preferenceManager = PreferenceManager(applicationContext)

        val drawerLayout: DrawerLayout = binding.drawerLayout

        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        //Ini untuk mengakses navigasi dari drawer
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_profile,
            ), drawerLayout
        )

        //////////////////// Logout ///////////////////
        val navLogout: MenuItem = navView.menu.findItem(R.id.nav_logout)
        navLogout.setOnMenuItemClickListener (object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                logOut()
                return true
            }
        })

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                sendFCMTokenToDatabase(task.result!!.token)
            }
        }
    }

    /////////////// mengambil item yang ada pada menu.xml
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //memberikan perintah, ketika tombol kaca pembesar ditekan maka akan Pindah Activity
        if (item.itemId == R.id.search) {
            val intent = Intent(this, PsychologActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //////////////////////////////token buat user nanti pas login dan logout
    private fun sendFCMTokenToDatabase(token: String) {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val documentReference: DocumentReference = database.collection(KEY_PATIENTS).document(preferenceManager.getString(
            KEY_USER_ID))

        documentReference.update(KEY_FCM_TOKEN, token)
            .addOnSuccessListener {
                Toast.makeText(this, "Token Update", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Unable send token : " + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    /////////Logout
    private fun logOut() {
        Toast.makeText(this, "Logout.....", Toast.LENGTH_SHORT).show()
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val documentReference: DocumentReference = database.collection(KEY_PATIENTS).document(preferenceManager.getString(KEY_USER_ID))

        val updates = HashMap<String, String>()
        updates[KEY_FCM_TOKEN] = FieldValue.delete().toString()
        documentReference.update(updates as Map<String, Any>)
            .addOnCompleteListener {
                preferenceManager.clearPreferences()
                startActivity(Intent(this, AfterSplashActivity::class.java))
                finish()
            }

            .addOnFailureListener {
                Toast.makeText(this, "Failde Logout", Toast.LENGTH_SHORT).show()
            }
    }
}