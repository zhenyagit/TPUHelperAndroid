package com.example.tpuhelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tpuhelper.entity.TwoWords


class StateAdapter internal constructor(context: Context?, states: List<TwoWords>) :
    RecyclerView.Adapter<StateAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val states: List<TwoWords>

    init {
        this.states = states
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.activity_founded_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val twoWords: TwoWords = states[position]
        when (twoWords.iconNumber) {
            1 -> holder.iconView.setImageResource(R.drawable.ic_icon_course)
            2 -> holder.iconView.setImageResource(R.drawable.ic_icon_quiz)
            3 -> holder.iconView.setImageResource(R.drawable.ic_icon_exe)
            else -> holder.iconView.setImageResource(R.drawable.ic_icon_course)
        }
        holder.nameView.text = twoWords.name
        holder.descriptionView.text = twoWords.description
    }

    override fun getItemCount(): Int {
        return states.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val iconView: ImageView
        val nameView: TextView
        val descriptionView: TextView
        init {
            iconView = view.findViewById(R.id.itemIcon)
            nameView = view.findViewById(R.id.name)
            descriptionView = view.findViewById(R.id.description)
        }
    }
}