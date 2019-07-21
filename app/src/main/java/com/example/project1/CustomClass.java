package com.example.project1;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class CustomClass{
    Bitmap image;
    String name;
    CustomClass(Bitmap img, String n){
        image=img;
        name=n;
    }
    CustomClass(String n){
        name=n;
    }
    public String getName(){
        return name;
    }
    public void saveImage(Bitmap b){
        image=b;
    }
    public void set(CustomClass a){
        this.image=a.image;
        this.name=a.name;
    }
}
