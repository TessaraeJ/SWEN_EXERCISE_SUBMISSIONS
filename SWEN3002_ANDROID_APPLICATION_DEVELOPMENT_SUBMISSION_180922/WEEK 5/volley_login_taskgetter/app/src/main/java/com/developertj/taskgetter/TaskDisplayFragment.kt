package com.developertj.taskgetter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.card_task.view.*
import kotlinx.android.synthetic.main.fragment_task_display.*

class TaskDisplayFragment : Fragment(){
    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val taskRecyclerView = recyclerView_tasks
        taskRecyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    fun addTaskCard(task: Task) {
        groupAdapter.add(TaskCard(context!!, task ))
    }

    class TaskCard(private val context: Context, private val task: Task) : Item<ViewHolder>() {
        val TAG = "TaskCard"
        override fun getLayout(): Int = R.layout.card_task

        override fun notifyChanged() {
            super.notifyChanged()
            Log.d(TAG, "notified Changed.")
        }

        override fun bind(viewHolder: ViewHolder, position: Int) {
            val itemView = viewHolder.itemView

            itemView.textView_id.text = task.id.toString()
            itemView.textView_title.text = task.title
            itemView.textView_description.text = task.description

            if (task.done == true){
                itemView.textView_status.text = context.getString(R.string.TASK_COMPLETE)
                itemView.textView_status.setTextColor(ContextCompat.getColor(context, R.color.green))
            } else {
                itemView.textView_status.text = context.getString(R.string.TASK_INCOMPLETE)
                itemView.textView_status.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }
    }
}