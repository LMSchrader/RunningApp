package com.example.runningapp.adapters

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.runningapp.R
import com.example.runningapp.data.RunningScheduleEntry

class HomeAdapter(
    runHistoryLiveData: LiveData<List<RunningScheduleEntry>>,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var data: List<RunningScheduleEntry> = emptyList()

    init {
        runHistoryLiveData.observe(lifecycleOwner) { runningScheduleEntries ->
            data = runningScheduleEntries
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val itemCount = 3
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.home_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO: bilder einfügen mit Daten aus Datenbank
       holder.test.text = "Hier finden sich später Abbildungen"
    }

    override fun getItemCount(): Int {
        return HomeAdapter.itemCount
    }

    class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
            //TODO
            val test: TextView = itemView.findViewById(R.id.test)
        }

}