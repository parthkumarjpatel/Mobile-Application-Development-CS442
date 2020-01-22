package com.example.inspirationrewards;

public class ListItem {
    private String lnRV, fnRV,  positionRV, departmentRV,usr;
    private int pointsAwardedRV;
    private String Image;


    public int getPointsAwardedRV() {
        return pointsAwardedRV;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public ListItem(String lnRV, String fnRV, String positionRV, String departmentRV, String usr, int pointsAwardedRV, String Image) {
        this.lnRV = lnRV;
        this.fnRV = fnRV;
        this.usr=usr;
        this.positionRV = positionRV;
        this.departmentRV = departmentRV;
        this.pointsAwardedRV = pointsAwardedRV;
        this.Image=Image;
    }
    public ListItem(){

    }


    public void setLnRV(String lnRV) {
        this.lnRV = lnRV;
    }

    public void setFnRV(String fnRV) {
        this.fnRV = fnRV;
    }

    public void setPositionRV(String positionRV) {
        this.positionRV = positionRV;
    }

    public void setDepartmentRV(String departmentRV) {
        this.departmentRV = departmentRV;
    }

    public void setPointsAwardedRV(int pointsAwardedRV) {
        this.pointsAwardedRV = pointsAwardedRV;
    }

    public String getLnRV() {
        return lnRV;
    }

    public String getFnRV() {
        return fnRV;
    }

    public int getPonitsAwardedRV() {
        return pointsAwardedRV;
    }

    public String getPositionRV() {
        return positionRV;
    }

    public String getDepartmentRV() {
        return departmentRV;
    }

}
