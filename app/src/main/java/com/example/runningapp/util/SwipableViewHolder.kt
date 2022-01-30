package com.example.runningapp.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class SwipableViewHolder(itemView: View, val viewForeground: View) : RecyclerView.ViewHolder(itemView)