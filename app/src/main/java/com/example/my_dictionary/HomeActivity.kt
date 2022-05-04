package com.example.my_dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.Room.Saved
import com.example.my_dictionary.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var appDatabase: AppDatabase
    lateinit var datalist:ArrayList<Saved>
    lateinit var binding: ActivityHomeBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private val TAG = "HomeActivity"
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        setSupportActionBar(binding.toolbarMain)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)



        checkDatabase()
        setNavDrawer()
        setbottomnav()

    }

    private fun setbottomnav() {
        val navController = findNavController(R.id.hostFragment)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun setNavDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this, binding.drawerlayout, binding.toolbarMain,
            R.string.open, R.string.close
        )

        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerlayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));



        binding.navigationView.setNavigationItemSelectedListener { menuItem ->


            when (menuItem.itemId) {
                R.id.home_menu -> {

                      try {
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.setType("text/plain")
                    share.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    share.putExtra(
                        Intent.EXTRA_TEXT,
                        "Dasturimni ishlatganingizdan hursandman \n" +
                                "https://t.me/my_apps_android \n"


                    )
                    startActivity(Intent.createChooser(share, "Share word..."))

                } catch (e: java.lang.Exception){
                    Log.d(TAG, "setShare: ${e.message}")}


                    binding.drawerlayout.closeDrawers()
                }
                R.id.profile_menu -> {
                    Toast.makeText(this, "Done by Mr_KadiroFF", Toast.LENGTH_LONG).show()
                    binding.drawerlayout.closeDrawers()
                }

            }
            true
        }



    }

    private fun checkDatabase() {
        datalist = ArrayList()
        datalist = appDatabase.saveDao().getAllData() as ArrayList<Saved>

        if (datalist.isNullOrEmpty()){
            val saved = Saved()
            saved.data = "Saved"
            appDatabase.saveDao().insertData(saved)
        }
    }
}