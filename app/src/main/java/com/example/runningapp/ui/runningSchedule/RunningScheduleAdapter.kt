package com.example.runningapp.ui.runningSchedule

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.RunningScheduleEntryActivity
import com.example.runningapp.model.RunningScheduleEntry

class RunningScheduleAdapter : RecyclerView.Adapter<RunningScheduleAdapter.ViewHolder>() {

    // TODO: remove dummy data
    @RequiresApi(Build.VERSION_CODES.O)
    private val data = arrayListOf(RunningScheduleEntry.StaticFunctions.getDummyData(), RunningScheduleEntry.StaticFunctions.getDummyData())
    private val weekdays = arrayListOf("Mo Fr", "Di Fr So")

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title : TextView = view.findViewById(R.id.title)
        val period : TextView = view.findViewById(R.id.period)
        val weekdays : TextView = view.findViewById(R.id.weekdays)

        init {
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener {
                val position : Int = adapterPosition
                //TODO: edit
                val intent = Intent(view.context, RunningScheduleEntryActivity::class.java).apply {}
                view.context.startActivity(intent)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.running_schedule_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = data[position].getTitle()
        viewHolder.period.text = data[position].getStartDate().toString() + " - " + data[position].getEndDate().toString()
        viewHolder.weekdays.text = weekdays[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount() = data.size

}

