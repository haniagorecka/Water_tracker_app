package com.example.watertrackerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecycleViewAdapter (private val List: MutableList<RecycleViewData>): RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>() {

    var onItemClick: ((RecycleViewData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = List[position]
        holder.dateView.text = currentItem.date
        holder.amountView.text = currentItem.amount + " ml"
        holder.progressBar.max = 100
        //ObjectAnimator.ofInt(holder.progressBar, "progress", 0).setDuration(2000).start()
        holder.progressBar.progress = currentItem.percent

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(currentItem)
        }

    }

    override fun getItemCount() = List.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
       val dateView : TextView = itemView.findViewById(R.id.DateRecycle)
       val amountView: TextView = itemView.findViewById(R.id.AmountRecycle)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar2)
    }
}