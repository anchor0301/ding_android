package com.example.myapplication.ListDog;

public class DogItem {
    String DogName; //애견 이름
    String breed;   //견종
    String lastNum; //전화번호
    String sex;     //성별
    String weight;  //몸무게
    String pageID;  //몸무게
    String totalDay;//총 예약 기간
    boolean isLunchBoxSelected;
    boolean isDinnerBoxSelected;

    public String getPageID() {
        return pageID;
    }

    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    public boolean getLunchBoxSelected() {
        return isLunchBoxSelected;
    }


    public void setLunchBoxSelected(boolean selected) {
        isLunchBoxSelected = selected;
    }

    public void setDinnerBoxSelected(boolean selected) {
        isDinnerBoxSelected = selected;
    }

    public boolean getDinnerBoxSelected() {
        return isDinnerBoxSelected;
    }


    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(String totalDay) {
        this.totalDay = totalDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDogName() {
        return DogName;
    }

    public void setDogName(String dogName) {
        DogName = dogName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getLastNum() {
        return lastNum;
    }

    public void setLastNum(String lastNum) {
        this.lastNum = lastNum;
    }
}
