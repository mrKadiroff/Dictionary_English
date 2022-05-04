package com.example.my_dictionary.bottom_navs

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.my_dictionary.R
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.ViewPagerAdapter
import com.example.my_dictionary.databinding.FragmentHomeBinding
import com.example.my_dictionary.databinding.FragmentSearchBinding
import com.example.my_dictionary.models.Dictionary
import com.example.my_dictionary.viewmodels.Status
import com.example.my_dictionary.viewmodels.UserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment(),MediaPlayer.OnPreparedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentSearchBinding
    lateinit var appDatabase: AppDatabase
    lateinit var userViewModel: UserViewModel
    var myWord: Dictionary? = null
    var music: String? = null
    var soz: String? = null
    var mediaPlayer: MediaPlayer? = null
    private val TAG = "SearchFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(binding.root.context)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setViewPager()
        setUI()
        setMusic()

        return binding.root
    }

    private fun setMusic() {
        binding.voice.setOnClickListener {
            if (myWord != null) {
                try {
//                    Log.d(TAG, "onCreateView: audi link = ${myWord!!.phonetics[0].audio}")
                    if (myWord!!.phonetics.get(0).audio.isNotEmpty()) {
                        Log.d(TAG, "onCreateView: $music")
                        setPlay(myWord!!.phonetics.get(0).audio)
                    } else if (myWord!!.phonetics.get(1).audio.isNotEmpty()) {
                        setPlay(myWord!!.phonetics.get(1).audio)
                    } else {
                        Toast.makeText(requireContext(), "no fucking sound", Toast.LENGTH_SHORT)
                            .show()
                    }


                    Toast.makeText(context, "Audio started", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Audio error ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else if (music != null) {
                try {


                    setPlay(music!!)



                    Toast.makeText(context, "Audio started", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(context, "Audio error ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setUI() {
        val allWords = appDatabase.wordDao().getAllWords()

        allWords.forEach {
            binding.wordd.text = it.word
        }

        val wordd: String =binding.wordd.getText().toString()
        GlobalScope.launch(Dispatchers.Main) {
            userViewModel.getWord(wordd)
                .observe(viewLifecycleOwner) {






                    when (it.status) {
                        Status.LOADING -> {

                        }

                        Status.ERROR -> {

                        }

                        Status.SUCCESS -> {

                            myWord = it.data?.get(0)
//                            binding.trans.text = myWord!!.phonetics!![0].text


                            Log.d(TAG, "setEdit: ${myWord!!.phonetics!![0].text}")
                            music = myWord!!.phonetics[0].audio
                            var list = it.data



                        }
                    }



                }



        }


    }

    private fun setViewPager() {
        val allWords = appDatabase.wordDao().getAllWords()




        val adapter= com.example.my_dictionary.adapters.ViewPagerAdapter("Null",childFragmentManager,lifecycle)
        binding.viewPager.adapter=adapter







        TabLayoutMediator(binding.tablayout,binding.viewPager){tab,position->
            when(position){
                0->{
                    tab.text="Definition"

                }
                1->{
                    tab.text=" Thesaurus"
                }
            }
        }.attach()

    }

    private fun setPlay(music2: String) {
        if (music2!!.length > 5 && music2!!.substring(music2!!.length - 3, music2!!.length)
                .equals("mp3")
        ) {
            mediaPlayer = MediaPlayer()
            Log.d(TAG, "setPlay: $music2")
            val song = "https://api.dictionaryapi.dev/media/pronunciations/en/gay-us.mp3"
            mediaPlayer?.setDataSource(music2)
            mediaPlayer?.setOnPreparedListener(this)
            mediaPlayer?.prepareAsync()
        } else {
            Toast.makeText(requireContext(), "Music format exception", Toast.LENGTH_SHORT)
                .show()
        }



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }
}