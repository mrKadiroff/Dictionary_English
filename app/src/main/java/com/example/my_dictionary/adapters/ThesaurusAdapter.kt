package com.example.my_dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_dictionary.databinding.AllThesurursBinding
import com.example.my_dictionary.models.Definition
import com.example.my_dictionary.models.Thesaurus

class ThesaurusAdapter(var list: List<String>) : RecyclerView.Adapter<ThesaurusAdapter.Vh>() {

    inner class Vh(var allDefinitionBinding: AllThesurursBinding) :
        RecyclerView.ViewHolder(allDefinitionBinding.root){

        fun onBind(word: String) {
            allDefinitionBinding.Antony.text = word

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(AllThesurursBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


}