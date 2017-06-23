package com.example.raj.helpline;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class feedback_completed extends AppCompatActivity {
    Toolbar toolbar;
    TextView task_id, task_category, question,answer;
    RatingBar rating;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_completed);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ctx = this;
        setSupportActionBar(toolbar);
        task_id = (TextView) findViewById(R.id.task_id);
        task_category = (TextView) findViewById(R.id.task_category);
        question = (TextView) findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);
        rating = (RatingBar) findViewById(R.id.rating);

        task_id.setText("Task Id:" + getIntent().getStringExtra("task_id"));
        task_category.setText("Category:" + getIntent().getStringExtra("task_category"));
        question.setText("Question:" + getIntent().getStringExtra("question"));
        answer.setText("Answer:" + getIntent().getStringExtra("answer"));
        rating.setRating(Float.parseFloat(getIntent().getStringExtra("rating")));
    }
}
