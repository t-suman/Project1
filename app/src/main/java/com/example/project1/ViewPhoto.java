package com.example.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewPhoto extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        imageView=findViewById(R.id.imageView3);

        //Bundle extras=getIntent().getExtras();
        //Bitmap bitmap=(Bitmap)extras.getParcelable("imagebitmap");
        //imageView.setImageBitmap(bitmap);

        Intent intent=getIntent();
        byte[] byteArr=intent.getByteArrayExtra("image");
        Bitmap bitmap= BitmapFactory.decodeByteArray(byteArr,0,byteArr.length);
        imageView.setImageBitmap(bitmap);

    }
    public void update(View v){

    }
}
