package com.example.my_dictionary.bottom_navs

import android.R.attr
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.my_dictionary.MainActivity
import com.example.my_dictionary.R
import com.example.my_dictionary.databinding.FragmentHomeBinding
import com.example.my_dictionary.models.Dictionary
import com.example.my_dictionary.retrofit.RetrofitClient
import com.example.my_dictionary.viewmodel2.MyEditData
import com.example.my_dictionary.viewmodels.Status
import com.example.my_dictionary.viewmodels.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.R.attr.label

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Build
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.Room.Saved
import com.example.my_dictionary.Room.Word
import com.example.my_dictionary.adapters.WordAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(),MediaPlayer.OnPreparedListener {
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

    lateinit var binding: FragmentHomeBinding
    private val TAG = "HomeFragment"
    lateinit var userViewModel: UserViewModel
    lateinit var myLiveData: MyEditData
    var mediaPlayer: MediaPlayer? = null
    lateinit var appDatabase: AppDatabase
    lateinit var wordAdapter: WordAdapter
    var music: String? = null
    var soz: String? = null
    var transkr: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        setEdit()
        setRv()





        return binding.root

    }

    private fun setRv() {
        val allWords = appDatabase.wordDao().getAllWords()

        wordAdapter = WordAdapter(allWords)
        binding.itemsRv.adapter = wordAdapter
    }

    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard =
                context.getSystemService(CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = text

        } else {
            val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }
    }


    private fun setPlay(music2: String) {
        binding.audiolash.setOnClickListener {
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
    }

    private fun setEdit() {
        myLiveData = MyEditData()
        myLiveData.get().observe(this, Observer {
            binding.word.text = it
            val word = it

            binding.nushalash.setOnClickListener {
                setClipboard(binding.root.context, word)
                Toast.makeText(binding.root.context, "Text copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }


        })
        binding.edittext.addTextChangedListener {
            myLiveData.set(it.toString())
        }


        binding.edittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Startsearch(binding.edittext.text.toString())


            }
            true
        }

    }

    fun Startsearch(word: String) {
        userViewModel.getWord(word).observe(this, Observer {

            when (it.status) {
                Status.LOADING -> {

                }

                Status.ERROR -> {

                }

                Status.SUCCESS -> {

                    var list = it.data

//                    Log.d(TAG, "Startsearch: ${it.data?.get(0)?.phonetics!![0]!!.text}")


                    soz = word
                    transkr= it.data?.get(0)?.phonetics!![0]!!.text
                    insertToRomm()







                    list!!.forEach {
                        binding.word.text = it.phonetics[0].text
                        music = it.phonetics[0].audio
                        setPlay(it.phonetics[0].audio)


                    }


                }
            }


        })
    }

    private fun insertToRomm() {
        val wordi = Word()
        wordi.word = soz
        wordi.transcription = transkr
        Log.d(TAG, "insertToRomm: ${soz} ${transkr}")
        wordi.saved = false
        appDatabase.wordDao().insertWord(wordi)





    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
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