package com.example.my_dictionary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.my_dictionary.databinding.FragmentTabBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    lateinit var binding: FragmentTabBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTabBinding.inflate(layoutInflater,container,false)

        setText()

        return binding.root
    }

    private fun setText() {
        if (param1 == 0){
            binding.tekst.text = "Ingliz tilidagi so'zlarni tez va oson \ntopa olasiz"
        }
        if (param1 == 1){
            binding.tekst.text = "Qo'shimcha tarzda so'zlarning talaffuzini \neshitish va transkripsiyasini o'qish imkoniyatiga ega bo'lasiz"
        }

        if (param1 == 2){
            binding.tekst.text = "Qidiruv tizimi yordamida deyarli barcha \ningliz tilidagi so'zlarni definition(ta'rif)ini bilib olasiz"
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            TabFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)

                }
            }
    }
}