package com.example.runningapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.data.RunningScheduleEntry
import com.example.runningapp.util.SwipeableViewHolder

class RunningScheduleRecyclerViewAdapter(
    liveData: LiveData<List<RunningScheduleEntry>>,
    private val onClickListener: (position: Int?) -> Unit,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RunningScheduleRecyclerViewAdapter.ViewHolder>() {

    private var data: List<RunningScheduleEntry> = emptyList()

    init {
        liveData.observe(lifecycleOwner) { entries ->
            data = entries
            notifyDataSetChanged()
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, private val onClickListener: (position: Int?) -> Unit) :
        SwipeableViewHolder(view, view.findViewById(R.id.view_foreground)) {
        val context: Context = view.context

        val title: TextView = view.findViewById(R.id.title)
        val startDate: TextView = view.findViewById(R.id.startDate)
        val endDate: TextView = view.findViewById(R.id.endDate)
        val weekdays: TextView = view.findViewById(R.id.weekdays)

        init {
            view.setOnClickListener {
                onClickListener.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.running_schedule_item, viewGroup, false)

        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = data[position].title
        viewHolder.startDate.text = data[position].startDate.toString()
        viewHolder.endDate.text = data[position].endDate.toString()
        viewHolder.weekdays.text = data[position].getWeekdayString(viewHolder.context)
    }

    override fun getItemCount() = data.size

}

