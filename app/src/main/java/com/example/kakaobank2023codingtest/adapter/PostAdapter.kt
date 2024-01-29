package com.example.kakaobank2023codingtest.adapter

import android.content.ClipData.Item
import android.content.DialogInterface.OnClickListener
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
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface ItemClick {
        fun onClick(item: PostModel)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            item.isFavorite = !item.isFavorite
            if (item.isFavorite) {
                holder.binding.ivFavorite.visibility = View.VISIBLE
            } else {
                holder.binding.ivFavorite.visibility = View.INVISIBLE
            }
            println("Item clicked - ID: ${item.id}, isFavorite: ${item.isFavorite}")
            itemClick?.onClick(item)
        }
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
                ivFavorite.visibility = if (item.isFavorite) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        }
    }
}
