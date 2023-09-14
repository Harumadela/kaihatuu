package com.example.kaihatu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.cachapa.expandablelayout.ExpandableLayout

class MyAdapter :
    ListAdapter<MyItem, MyAdapter.MyViewHolder>(MyDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: MyItem = getItem(position)
        holder.bind(item)
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var currentItem: MyItem? = null
        private val title: TextView = itemView.findViewById(R.id.title)
        private val detail: TextView = itemView.findViewById(R.id.detail)
        private val expandedLayout: ExpandableLayout = itemView.findViewById(R.id.expandable_layout)
        private val titleLayout: LinearLayout = itemView.findViewById(R.id.title_layout)
        private val arrow: ImageView = itemView.findViewById(R.id.expand_arrow)

        init {
            itemView.setOnClickListener {
                currentItem?.let {
                    val expanded = it.isExpanded
                    it.isExpanded = expanded.not()
                    titleLayout.isSelected = expanded.not()

                    expandedLayout.toggle()

                    val anim = RotateAnimation(
                        0f,
                        180f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f
                    ).apply {
                        duration = 300
                        fillAfter = true
                    }
                    arrow.startAnimation(anim)
                }
            }
        }

        fun bind(item: MyItem) {
            currentItem = item

            title.text = currentItem?.title
            detail.text = currentItem?.detail

            if (item.isExpanded) {
                expandedLayout.expand(false)
            } else {
                expandedLayout.collapse(false)
            }
            titleLayout.isSelected = item.isExpanded.not()
        }
    }
}

object MyDiffCallback : DiffUtil.ItemCallback<MyItem>() {
    override fun areItemsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MyItem, newItem: MyItem): Boolean {
        return oldItem.id == newItem.id
    }
}