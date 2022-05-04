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
import android.app.Activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.Room.Saved
import com.example.my_dictionary.Room.Word
import com.example.my_dictionary.adapters.WordAdapter
import com.example.my_dictionary.databinding.ItemMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Error
import java.text.SimpleDateFormat
import java.util.*


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
    var myWord: Dictionary? = null
    private val RQ_SPEECH_REC = 102
    var myWordDb: Word? = null
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
        setSound()
        setConverter()
        setShare()
        setSave()
        setCopy()












        return binding.root

    }

    private fun setCopy() {
        val wordd=binding.edittext.text.toString()

    }

    private fun setSave() {
        val wordd: String =binding.word.getText().toString()
        val allWords = appDatabase.wordDao().getAllWords()
        binding.saqlash.setOnClickListener {

            for (wordDb in allWords) {
                if (wordDb.word == wordd){
                    if (wordDb.saved == false){
                        val wordByName = appDatabase.wordDao().getWordByName(wordd)
                        wordByName.saved = true
                        appDatabase.wordDao().updateTalaba(wordByName)
                        Toast.makeText(binding.root.context, "added", Toast.LENGTH_SHORT).show()
                    }else{
                        val wordByName = appDatabase.wordDao().getWordByName(wordd)
                        wordByName.saved = false
                        appDatabase.wordDao().updateTalaba(wordByName)
                        Toast.makeText(binding.root.context, "removed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    }

    private fun setShare() {
        val allWords = appDatabase.wordDao().getAllWords()



        binding.ulashish.setOnClickListener {
            if (myWord != null) {
                try {
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.setType("text/plain")
                    share.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    share.putExtra(
                        Intent.EXTRA_TEXT,
                        "${myWord!!.word} \n" +
                                "${myWord!!.meanings[0].definitions[0].definition} \n" +
                                "${myWord!!.meanings[0].definitions[0].example} \n"

                    )
                    startActivity(Intent.createChooser(share, "Share word..."))

                } catch (e: java.lang.Exception){
                    Log.d(TAG, "setShare: ${e.message}")
                }
            }else if (!allWords.isNullOrEmpty()){
                try {

                    val wordd: String =binding.word.getText().toString()
                    val defintion: String = binding.definitionnnnnn.getText().toString()
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    share.putExtra(
                        Intent.EXTRA_TEXT,
                        "${wordd} \n" +
                                "${defintion}"

                    )
                    startActivity(Intent.createChooser(share, "Share word ..."))
                } catch (e: Exception) {
                    Log.e(TAG, "onCreateView: ${e.message}")
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result :ArrayList<String>? = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val word = result?.get(0).toString()
            binding.edittext.setText(word)
        }
    }



    private fun setConverter() {

        binding.microphone.setOnClickListener {
            if (!SpeechRecognizer.isRecognitionAvailable(binding.root.context)){
                Toast.makeText(binding.root.context, "Speech recognition is not available", Toast.LENGTH_SHORT).show()
            } else {
                val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault())
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something!!!")
                startActivityForResult(i,RQ_SPEECH_REC)
            }
        }


    }

    private fun setSound() {
        binding.audiolash.setOnClickListener {
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

    private fun setRv() {
        val allWords = appDatabase.wordDao().getAllWords()

        wordAdapter = WordAdapter(allWords,object :WordAdapter.OnItemClickListener{
            var a = 100
            override fun onFavouriteClick(
                itemMainBinding: ItemMainBinding,
                word: Word,
                position: Int
            ) {
                itemMainBinding.bookmarkBtn.setImageResource(R.drawable.ic_baseline_bookmark_24)
                if (a == position) {
                    itemMainBinding.bookmarkBtn.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    word.saved = true
                    appDatabase.wordDao().updateTalaba(word)
                    a++
                } else {
                    itemMainBinding.bookmarkBtn.setImageResource(R.drawable.saved)
                    word.saved = false
                    appDatabase.wordDao().updateTalaba(word)
                    a = position
                }
            }

        })
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

    private fun setEdit() {



        binding.sana.text = SimpleDateFormat("dd.MM.yyyy").format(Date())
        val allWords = appDatabase.wordDao().getAllWords()
        allWords.forEach {
            binding.word.text = it.word
            binding.definitionnnnnn.text = it.transcription
        }

        myLiveData = MyEditData()
        myLiveData.get().observe(this, Observer {
            binding.word.text = it
            val word = it

            binding.nushalash.setOnClickListener {
                setClipboard(binding.root.context, word)
                Toast.makeText(binding.root.context, "Text copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
            }

            binding.copy.setOnClickListener {
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




                GlobalScope.launch(Dispatchers.Main) {
                    userViewModel.getWord(binding.edittext.text.toString())
                        .observe(viewLifecycleOwner) {






                        when (it.status) {
                            Status.LOADING -> {

                            }

                            Status.ERROR -> {
                                Toast.makeText(
                                    binding.root.context,
                                    "Word not found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            Status.SUCCESS -> {

                                try {
                                    myWord = it.data?.get(0)
                                    binding.trans.text = myWord!!.phonetics!![0].text
                                    binding.definitionnnnnn.text = myWord!!.meanings[0].definitions[0].definition
                                    music = myWord!!.phonetics[0].audio

                                    Log.d(TAG, "setEdit: $music")

                                    var list = it.data

                                }catch (e:java.lang.Exception){
                                    Toast.makeText(
                                        binding.root.context,
                                        "Word not found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }






                            }
                        }



                    }



                }

                Handler(Looper.getMainLooper()).postDelayed({
                    insertToRomm()
                },2000)







            }
            true
        }

    }

    private fun setSong(uri:String) {

    }



    private fun insertToRomm() {

        try {
            val wordi = Word()
            val wordd: String =binding.word.getText().toString()
            val transkr = binding.trans.getText().toString()
            wordi.word = wordd
            wordi.uri = myWord!!.phonetics.get(0).audio
            wordi.transcription = transkr
            wordi.saved = false
            appDatabase.wordDao().insertWord(wordi)

        }catch (e:java.lang.Exception){
            Toast.makeText(binding.root.context, "${e.message}", Toast.LENGTH_SHORT).show()
        }








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