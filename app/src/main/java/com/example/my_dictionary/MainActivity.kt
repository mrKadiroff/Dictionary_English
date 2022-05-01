package com.example.my_dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.Room.Saved
import com.example.my_dictionary.databinding.ActivityMainBinding
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class MainActivity : AppCompatActivity() {
    val animalsArray = arrayOf(
        "Cat",
        "Dog",
        "Bird"
    )
    lateinit var binding: ActivityMainBinding
    lateinit var appDatabase: AppDatabase
    lateinit var datalist:ArrayList<Saved>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        checkDatabase()

        setOnboard()

    }

    private fun setOnboard() {
        val viewPager2 = binding.viewPager2
        val adapter = ViewPagerAdapter(animalsArray,supportFragmentManager, lifecycle)
        viewPager2.adapter = adapter
        val wormDotsIndicator = binding.springDotsIndicator
        wormDotsIndicator.setViewPager2(viewPager2)

        viewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (viewPager2.currentItem + 1 == adapter.itemCount) {
                    binding.startt.visibility = View.VISIBLE
                    binding.skipp.visibility = View.GONE
                    binding.startt.setOnClickListener {
                        navigateToHomeActivity()
                    }
                }else{
                    binding.startt.visibility = View.GONE
                    binding.skipp.visibility = View.VISIBLE
                }



            }



        })

        binding.skipp.setOnClickListener {
            navigateToHomeActivity()
        }
    }

    private fun checkDatabase() {
        datalist = ArrayList()
        datalist = appDatabase.saveDao().getAllData() as ArrayList<Saved>

        if (!datalist.isNullOrEmpty()){
            navigateToHomeActivity()
        }
    }

    private fun navigateToHomeActivity() {
        startActivity(Intent(applicationContext, HomeActivity::class.java))
        finish()
    }
}