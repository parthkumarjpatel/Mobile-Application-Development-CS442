package com.example.inspirationrewards;

public class Compare implements java.util.Comparator<ListItem> {
    public int compare(ListItem l1, ListItem l2) {
        if (l1.getPointsAwardedRV() == l2.getPointsAwardedRV()) {
            return l1.getLnRV().compareTo(l2.getLnRV())==1 ? -1 : 1;
        } else
             return l1.getPointsAwardedRV() > l2.getPointsAwardedRV()? -1 : 1;
    }
}
