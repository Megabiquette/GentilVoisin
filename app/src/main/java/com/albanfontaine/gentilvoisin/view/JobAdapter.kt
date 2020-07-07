package com.albanfontaine.gentilvoisin.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albanfontaine.gentilvoisin.R
import com.albanfontaine.gentilvoisin.model.Job
import com.albanfontaine.gentilvoisin.repository.UserRepository

class JobAdapter(
    private val context: Context,
    private val jobList: List<Job>,
    private val onItemListener: OnItemListener,
    private val userRepository: UserRepository = UserRepository
) : RecyclerView.Adapter<JobViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_jobs_recycler_view, parent, false)
        return JobViewHolder(view, onItemListener)
    }

    override fun getItemCount(): Int = jobList.size

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.updateWithJob(context, jobList[position], userRepository)
    }

    interface OnItemListener {
        fun onItemClicked(position: Int)
    }
}