package com.example.runningapp.ui.runningSchedule

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.model.RunningScheduleEntry

class RunningScheduleAdapter(private val liveData: LiveData<List<RunningScheduleEntry>>, private val onClickListener: (position: Int?) -> Unit, private val lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>() {

    // TODO: remove dummy data
    @RequiresApi(Build.VERSION_CODES.O)
    private val data = RunningScheduleEntry.StaticFunctions.getDummyData()
    private val weekdays = arrayListOf("Mo Fr", "Di Fr So")

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View,  private val onClickListener: (position: Int?) -> Unit) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.title)
        val startDate : TextView = view.findViewById(R.id.startDate)
        val endDate : TextView = view.findViewById(R.id.endDate)
        val weekdays : TextView = view.findViewById(R.id.weekdays)

        init {
            view.setOnClickListener {
                onClickListener.invoke(adapterPosition)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.running_schedule_item, viewGroup, false)

        return ViewHolder(view, onClickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = data[position].getTitle()
        viewHolder.startDate.text = data[position].getStartDate().toString()
        viewHolder.endDate.text = data[position].getEndDate().toString()
        viewHolder.weekdays.text = weekdays[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount() = data.size

}

