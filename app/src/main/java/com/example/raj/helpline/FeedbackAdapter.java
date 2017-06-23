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
 * Created by raj on 6/8/2017.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();
    Context ctx;

    public FeedbackAdapter(ArrayList<Feedback> feedbacks, Context ctx ){

        this.feedbacks = feedbacks;
        this.ctx = ctx;
    }
    @Override
    public FeedbackAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_view,parent,false);
        FeedbackAdapter.FeedbackViewHolder feedbackViewHolder = new FeedbackAdapter.FeedbackViewHolder(view,ctx,feedbacks);
        return feedbackViewHolder;
    }

    @Override
    public void onBindViewHolder(FeedbackAdapter.FeedbackViewHolder holder, int position) {

        Feedback t = feedbacks.get(position);
        holder.feedback_img.setImageResource(t.getImage_id());
        holder.task_id.setText("Task Id:"+t.getTask_id());
        holder.task_category.setText("Category:"+t.getTask_category());

    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView feedback_img;
        TextView task_id;
        TextView task_category;
        ArrayList<Feedback> feedbacks = new ArrayList<Feedback>();
        Context ctx;

        public FeedbackViewHolder(View view,Context ctx,ArrayList<Feedback> feedbacks)
        {
            super(view);
            view.setOnClickListener(this);
            this.feedbacks = feedbacks;
            this.ctx = ctx;
            feedback_img = (ImageView) view.findViewById(R.id.feedback_img);
            task_id = (TextView) view.findViewById(R.id.task_id);
            task_category = (TextView) view.findViewById(R.id.task_category);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Feedback feedback = this.feedbacks.get(position);
            if(feedback.getFeedback_type().equals("pending")){
                Intent intent = new Intent(this.ctx,feedback_pending.class);
                intent.putExtra("task_id",feedback.getTask_id());
                intent.putExtra("task_category",feedback.getTask_category());
                intent.putExtra("question",feedback.getQuestion());
                intent.putExtra("answer",feedback.getAnswer());
                ctx.startActivity(intent);
            }

            if(feedback.getFeedback_type().equals("completed")){
                Intent intent = new Intent(this.ctx,feedback_completed.class);
                intent.putExtra("task_id",feedback.getTask_id());
                intent.putExtra("task_category",feedback.getTask_category());
                intent.putExtra("question",feedback.getQuestion());
                intent.putExtra("answer",feedback.getAnswer());
                intent.putExtra("rating",feedback.getRating());
                ctx.startActivity(intent);
            }

        }
    }
}
