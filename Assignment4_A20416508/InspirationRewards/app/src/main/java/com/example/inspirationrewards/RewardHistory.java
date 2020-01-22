package com.example.inspirationrewards;

import java.io.Serializable;
import java.util.Date;
import java.util.Comparator;

public class RewardHistory implements Serializable, Comparator<RewardHistory> {
   // private Date date;
    private String name,comments,sdate;
    private int pointsrr;



    public RewardHistory(String name, String comments, String sdate, int pointsrr) {
       // this.date = date;
        this.name = name;
        this.comments = comments;
        this.pointsrr = pointsrr;
        this.sdate=sdate;
    }
    public RewardHistory(){

    }
    public void setName(String name) {
        this.name = name;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPointsrr(int pointsrr) {
        this.pointsrr = pointsrr;
    }

    public String getSdate() {
        return sdate;
    }

    public String getName() {
        return name;
    }
//    //public Date getDate() {
//        return date;
//    }

    public String getComments() {
        return comments;
    }

    public int getPointsrr() {
        return pointsrr;
    }
    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    @Override
    public int compare(RewardHistory o1, RewardHistory o2) {
        return o1.getSdate().compareTo(o2.getSdate())==1 ? -1 : 1;
    }
//    public void setDate(Date date) {
//        this.date = date;
//    }




}
