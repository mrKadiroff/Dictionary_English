package com.example.my_dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_dictionary.R
import com.example.my_dictionary.Room.Word
import com.example.my_dictionary.databinding.ItemMainBinding
import com.example.my_dictionary.databinding.ItemSeenBinding

class WordAdapter(var list: List<Word>,var onItemClickListener: WordAdapter.OnItemClickListener) : RecyclerView.Adapter<WordAdapter.Vh>() {

    inner class Vh(var itemMainBinding: ItemMainBinding) :
        RecyclerView.ViewHolder(itemMainBinding.root){

        fun onBind(word: Word) {
          itemMainBinding.text.text = word.word
            itemMainBinding.transcription.text = word.transcription

            if (word.saved == false){
                itemMainBinding.bookmarkBtn.setImageResource(R.drawable.saved)
            }else{
                itemMainBinding.bookmarkBtn.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }

            itemMainBinding.bookmarkBtn.setOnClickListener {
                onItemClickListener.onFavouriteClick(itemMainBinding,word,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size
    interface OnItemClickListener{
        fun onFavouriteClick(itemMainBinding: ItemMainBinding, word: Word, position: Int)
    }

}