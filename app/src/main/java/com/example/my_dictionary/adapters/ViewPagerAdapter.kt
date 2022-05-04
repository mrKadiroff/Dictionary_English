package com.example.my_dictionary.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.my_dictionary.TabFragment
import com.example.my_dictionary.tab_fragments.DefinitionFragment
import com.example.my_dictionary.tab_fragments.ExampleFragment

class ViewPagerAdapter (var word:String,fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                DefinitionFragment.newInstance(word,word)
            }
            1->{
                ExampleFragment.newInstance(word,word)
            }
            else->{
                Fragment()
            }
        }
    }


}