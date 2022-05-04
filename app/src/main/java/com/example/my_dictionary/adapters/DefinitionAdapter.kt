package com.example.my_dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_dictionary.databinding.AllDefinitionBinding
import com.example.my_dictionary.models.Definition

class DefinitionAdapter(var list: List<Definition>) : RecyclerView.Adapter<DefinitionAdapter.Vh>() {

    inner class Vh(var allDefinitionBinding: AllDefinitionBinding) :
        RecyclerView.ViewHolder(allDefinitionBinding.root){

        fun onBind(word: Definition) {
          allDefinitionBinding.definition.text = word.definition
            allDefinitionBinding.example.text = word.example

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(AllDefinitionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size


}