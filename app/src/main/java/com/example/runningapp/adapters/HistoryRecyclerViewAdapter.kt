package com.example.runningapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.runningapp.R
import com.example.runningapp.data.RunHistoryEntryMetaDataWithMeasurements
import com.example.runningapp.util.DateUtil.StaticFunctions.formatDate

class HistoryRecyclerViewAdapter(
    runHistoryLiveData: LiveData<List<RunHistoryEntryMetaDataWithMeasurements>>,
    private val onClickListener: (position: Int?) -> Unit,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>() {
    private var data: List<RunHistoryEntryMetaDataWithMeasurements> = emptyList()

    init {
        runHistoryLiveData.observe(lifecycleOwner) { runHistoryEntries ->
            data = runHistoryEntries
            notifyDataSetChanged()
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View, private val onClickListener: (position: Int?) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val introText: TextView = view.findViewById(R.id.intro_text)
        val dateTime: TextView = view.findViewById(R.id.date_time)

        init {
            introText.text = view.context.resources.getString(R.string.run_from)
            view.setOnClickListener {
                onClickListener.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.run_history_item, viewGroup, false)

        return ViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.dateTime.text = formatDate(data[position].metaData.date)
    }

    override fun getItemCount() = data.size
}