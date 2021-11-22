package com.example.runningapp.ui.history

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
import com.example.runningapp.model.RunHistoryEntry
import java.time.format.DateTimeFormatter

class HistoryAdapter(runHistoryLiveData: LiveData<List<RunHistoryEntry>>, private val onClickListener: (position: Int?) -> Unit, lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private var data: List<RunHistoryEntry> = emptyList()
    init {
        runHistoryLiveData.observe(lifecycleOwner) { runHistoryEntries ->
            data = runHistoryEntries
            notifyDataSetChanged()  // TODO: use notifyItemRangeChanged() instead
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, private val onClickListener: (position: Int?) -> Unit) : RecyclerView.ViewHolder(view) {
        val introText : TextView = view.findViewById(R.id.intro_text)
        val dateTime : TextView = view.findViewById(R.id.date_time)

        init {
            introText.text = view.context.resources.getString(R.string.intro_text)
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener {
                onClickListener.invoke(adapterPosition)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.run_history_item, viewGroup, false)

        return ViewHolder(view, onClickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val TS_DATE_PATTERN = "yyyy-MM-dd' 'HH:mm:ss"
        val formatter = DateTimeFormatter.ofPattern(TS_DATE_PATTERN)

        viewHolder.dateTime.text = data[position].getDate().format(formatter).toString()

    }

    // Return the size of your dataset (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount() = data.size
}