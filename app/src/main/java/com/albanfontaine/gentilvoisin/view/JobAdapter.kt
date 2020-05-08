package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Job

class JobAdapter(private val jobList: List<Job>, val context: Context): RecyclerView.Adapter<JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_jobs_recycler_view, parent, false)
        return JobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.updateWithJob(jobList[position], context)
    }
}