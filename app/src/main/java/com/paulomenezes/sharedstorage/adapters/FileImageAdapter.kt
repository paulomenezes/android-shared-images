package com.paulomenezes.sharedstorage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paulomenezes.sharedstorage.R
import com.paulomenezes.sharedstorage.databinding.ListImageItemBinding
import com.paulomenezes.sharedstorage.models.FileImage

class FileImageAdapter(
    private val list: MutableList<FileImage>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<FileImageAdapter.ViewHolder>() {
    class ViewHolder(private val listItem: ListImageItemBinding) : RecyclerView.ViewHolder(listItem.root) {
        fun bind(file: FileImage) {
            listItem.textTitle.text = file.name
            listItem.imageView.setImageURI(file.uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_image_item, parent, false)
        val binding = ListImageItemBinding.bind(view)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = list[position]

        holder.bind(file)

        holder.itemView.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}