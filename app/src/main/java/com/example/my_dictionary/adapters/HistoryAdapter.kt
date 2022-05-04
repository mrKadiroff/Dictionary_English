package com.example.my_dictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_dictionary.R
import com.example.my_dictionary.Room.Word
import com.example.my_dictionary.databinding.ItemMainBinding
import com.example.my_dictionary.databinding.ItemSeenBinding

class HistoryAdapter(var list: List<Word>,var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.Vh>() {

    inner class Vh(var itemSeenBinding: ItemSeenBinding) :
        RecyclerView.ViewHolder(itemSeenBinding.root){

        fun onBind(word: Word) {
            itemSeenBinding.text.text = word.word
            itemSeenBinding.transcription.text = word.transcription

            if (word.saved == false){
                itemSeenBinding.bookmark.setImageResource(R.drawable.saved)
            }else{
                itemSeenBinding.bookmark.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }

            itemSeenBinding.bookmark.setOnClickListener {
                onItemClickListener.onFavouriteClick(itemSeenBinding,word,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemSeenBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onFavouriteClick(itemSeenBinding: ItemSeenBinding, word: Word,position: Int)
    }


}