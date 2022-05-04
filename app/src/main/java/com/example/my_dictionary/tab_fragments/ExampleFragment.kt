package com.example.my_dictionary.tab_fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.adapters.ThesaurusAdapter
import com.example.my_dictionary.databinding.FragmentExampleBinding
import com.example.my_dictionary.models.Dictionary
import com.example.my_dictionary.viewmodels.Status
import com.example.my_dictionary.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExampleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExampleFragment : Fragment() {
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

    lateinit var binding: FragmentExampleBinding
    lateinit var appDatabase: AppDatabase
    lateinit var userViewModel: UserViewModel
    lateinit var thesaurusAdapter: ThesaurusAdapter
    private val TAG = "ExampleFragment"
    var myWord: Dictionary? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExampleBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(binding.root.context)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        setRV()





        return binding.root
    }

    private fun setRV() {
        val allWords = appDatabase.wordDao().getAllWords()
        allWords.forEach {
            binding.teksttsd.text = it.word
        }

        val wordd: String =binding.teksttsd.getText().toString()


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


                            Log.d(TAG, "setEdit: ${myWord!!.meanings[0].definitions}")
                            var list = it.data

                            thesaurusAdapter = ThesaurusAdapter(myWord!!.meanings[0].definitions[0].synonyms)
                            binding.rvAll.adapter = thesaurusAdapter


                        }
                    }



                }



        }



    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExampleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExampleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}