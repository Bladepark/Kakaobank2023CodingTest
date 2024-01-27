package com.example.kakaobank2023codingtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kakaobank2023codingtest.data.PostModel
import com.example.kakaobank2023codingtest.databinding.ItemRecyclerViewBinding

class PostAdapter : ListAdapter<PostModel, PostAdapter.ViewHolder>(DiffUtil) {

    companion object {
        private val DiffUtil = object : DiffUtil.ItemCallback<PostModel>() {
            override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(var binding: ItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostModel) {
            with(binding) {
                ivSearchImage.load(item.thumbnailUrl) {
                    crossfade(true)
                }
                tvPortalSite.text = item.siteName
                tvPostedTime.text = item.postedTime
                postItem.setOnClickListener {
                    if (item.isFavorite) {
                        ivFavorite.visibility = View.VISIBLE
                    } else {
                        ivFavorite.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}
