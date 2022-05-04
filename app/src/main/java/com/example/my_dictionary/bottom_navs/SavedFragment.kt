package com.example.my_dictionary.bottom_navs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.my_dictionary.R
import com.example.my_dictionary.Room.AppDatabase
import com.example.my_dictionary.Room.Word
import com.example.my_dictionary.adapters.HistoryAdapter
import com.example.my_dictionary.databinding.FragmentSavedBinding
import com.example.my_dictionary.databinding.FragmentSeenBinding
import com.example.my_dictionary.databinding.ItemSeenBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment() {
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

    lateinit var binding: FragmentSavedBinding
    lateinit var appDatabase: AppDatabase
    lateinit var historyAdapter: HistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        appDatabase = AppDatabase.getInstance(binding.root.context)

        setRV()


        return binding.root
    }

    private fun setRV() {
        val allWords = appDatabase.wordDao().getWordByStatus(true)

        historyAdapter = HistoryAdapter(allWords,object: HistoryAdapter.OnItemClickListener{
            var a = 100
            override fun onFavouriteClick(
                itemSeenBinding: ItemSeenBinding,
                word: Word,
                position: Int
            ) {
                itemSeenBinding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                if (a == position) {
                    itemSeenBinding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    word.saved = true
                    appDatabase.wordDao().updateTalaba(word)
                    a++
                } else {
                    itemSeenBinding.bookmark.setImageResource(R.drawable.saved)
                    word.saved = false
                    appDatabase.wordDao().updateTalaba(word)
                    a = position
                }
            }
        })
        binding.itemsRv.adapter = historyAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}