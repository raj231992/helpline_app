package com.example.raj.helpline;

/**
 * Created by raj on 9/13/2016.
 */

public class Task {
    public Task(String task_type,int image_id,String task_id,String task_category,String task_time,String task_date,String caller_name,String caller_location,String caller_number) {
        this.setTask_type(task_type);
        this.setImage_id(image_id);
        this.setTask_id(task_id);
        this.setTask_category(task_category);
        this.setTask_date(task_date);
        this.setTask_time(task_time);
        this.setCaller_name(caller_name);
        this.setCaller_location(caller_location);
        this.setCaller_number(caller_number);
    }
    private int image_id;
    private String task_id;
    private String task_date;
    private String task_time;
    private String caller_name;
    private String caller_number;
    private String caller_location;
    private String task_type;
    private String task_category;

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }



    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }



    public String getCaller_number() {
        return caller_number;
    }

    public void setCaller_number(String caller_number) {
        this.caller_number = caller_number;
    }

    public String getCaller_name() {
        return caller_name;
    }

    public void setCaller_name(String caller_name) {
        this.caller_name = caller_name;
    }

    public String getCaller_location() {
        return caller_location;
    }

    public void setCaller_location(String caller_location) {
        this.caller_location = caller_location;
    }



    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
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
