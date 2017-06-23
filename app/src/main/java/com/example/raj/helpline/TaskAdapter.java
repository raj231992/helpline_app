package com.example.raj.helpline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by raj on 9/13/2016.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    ArrayList<Task> tasks = new ArrayList<Task>();
    Context ctx;
    String task_type;

    public TaskAdapter(ArrayList<Task> tasks, Context ctx ){

        this.tasks = tasks;
        this.ctx = ctx;
    }
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view,parent,false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view,ctx,tasks);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        Task t = tasks.get(position);
        holder.task_img.setImageResource(t.getImage_id());
        holder.task_id.setText("Task Id:"+t.getTask_id());
        holder.task_time.setText("Time:"+t.getTask_time());
        holder.task_date.setText("Date:"+t.getTask_date());

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView task_img;
        TextView task_id;
        TextView task_time;
        TextView task_date;
        ArrayList<Task> tasks = new ArrayList<Task>();
        Context ctx;

        public TaskViewHolder(View view,Context ctx,ArrayList<Task> tasks)
        {
            super(view);
            view.setOnClickListener(this);
            this.tasks = tasks;
            this.ctx = ctx;
            task_img = (ImageView) view.findViewById(R.id.task_img);
            task_id = (TextView) view.findViewById(R.id.task_id);
            task_time = (TextView) view.findViewById(R.id.task_time);
            task_date = (TextView) view.findViewById(R.id.task_date);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Task task = this.tasks.get(position);
            if(task.getTask_type().equals("pending")){
                Intent intent = new Intent(this.ctx,activity_pending_task_details.class);
                intent.putExtra("task_id",task.getTask_id());
                intent.putExtra("task_category",task.getTask_category());
                intent.putExtra("task_date",task.getTask_date());
                intent.putExtra("task_time",task.getTask_time());
                intent.putExtra("caller_name",task.getCaller_name());
                intent.putExtra("caller_location",task.getCaller_location());
                ctx.startActivity(intent);
            }
            if(task.getTask_type().equals("accepted")){
                Intent intent = new Intent(this.ctx,activity_accepted_task_details.class);
                intent.putExtra("task_id",task.getTask_id());
                intent.putExtra("task_category",task.getTask_category());
                intent.putExtra("task_date",task.getTask_date());
                intent.putExtra("task_time",task.getTask_time());
                intent.putExtra("caller_name",task.getCaller_name());
                intent.putExtra("caller_location",task.getCaller_location());
                ctx.startActivity(intent);
            }

            if(task.getTask_type().equals("completed")){
                Intent intent = new Intent(this.ctx,activity_completed_task_details.class);
                intent.putExtra("task_id",task.getTask_id());
                intent.putExtra("task_category",task.getTask_category());
                intent.putExtra("task_date",task.getTask_date());
                intent.putExtra("task_time",task.getTask_time());
                intent.putExtra("caller_name",task.getCaller_name());
                intent.putExtra("caller_location",task.getCaller_location());
                ctx.startActivity(intent);
            }

        }
    }
}
