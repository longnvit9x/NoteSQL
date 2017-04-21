package com.example.vanlong.models;

import java.io.Serializable;

/**
 * Created by vanlong on 4/13/2017.
 */

public class Note implements Serializable {
    private boolean IsSelected;
    private int mId;
    private String mTitle;
    private String mContent;
    private String mDate;
    private boolean isCheckbokShow;

    public Note( String mTitle, String mContent, String mDate) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mDate = mDate;
        this.IsSelected=false;
        this.isCheckbokShow= false;

    }

    public Note() {
        this.IsSelected=false;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isSelected() {
        return IsSelected;
    }

    public void setSelected(boolean ISSelected) {
        this.IsSelected = ISSelected;
    }

    public boolean isCheckbokShow() {
        return isCheckbokShow;
    }

    public void setCheckbokShow(boolean checkbokShow) {
        isCheckbokShow = checkbokShow;
    }
}
