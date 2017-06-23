package com.example.raj.helpline;

/**
 * Created by raj on 6/8/2017.
 */

public class Feedback {
    public Feedback(String feedback_type,int image_id,String task_id,String task_category,String question,String answer,String rating) {
        this.setFeedback_type(feedback_type);
        this.setImage_id(image_id);
        this.setTask_id(task_id);
        this.setTask_category(task_category);
        this.setQuestion(question);
        this.setAnswer(answer);
        this.setRating(rating);

    }
    private int image_id;
    private String task_id;
    private String feedback_type;
    private String task_category;
    private String question;
    private String answer;
    private String rating;

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }



    public String getFeedback_type() {
        return feedback_type;
    }

    public void setFeedback_type(String feedback_type) {
        this.feedback_type = feedback_type;
    }



    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public int getImage_id() {

        return image_id;
    }

    public void setImage_id(int image_id) {

        this.image_id = image_id;
    }
}
