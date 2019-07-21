package com.example.project1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ViewOtherProfile extends AppCompatActivity implements View.OnClickListener {
    ImageView backgroundImage;
    ImageView profileImage;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_other_profile);

        Intent intent=getIntent();
        String userName=ParseUser.getCurrentUser().getUsername();//intent.getStringExtra("username");

        backgroundImage=findViewById(R.id.imageView7);
        profileImage=findViewById(R.id.profile_image2);
        name=findViewById(R.id.name111);
        name.setText(userName);
        updateProfilePic(userName,profileImage);

        profileImage.setOnClickListener(this);
        backgroundImage.setOnClickListener(this);
    }
    public void updateProfilePic(String user, final ImageView view){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ProfilePic");
        query.whereEqualTo("username", user);
        query.orderByDescending("updatedAt");
        try {
            if(query.count() == 0){
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.defaultprofile);
                view.setImageBitmap(b);


            }
            else{
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if(objects.size()!=0){
                                ParseFile file=objects.get(0).getParseFile("profilepic");

                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                        view.setImageBitmap(bitmap);

                                    }
                                });
                            }
                        }
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==profileImage.getId()||v.getId()==backgroundImage.getId()){
            View k= v;
            Intent intent=new Intent(ViewOtherProfile.this,ViewPhoto.class);
            k.buildDrawingCache();
            Bitmap image=k.getDrawingCache();
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray=stream.toByteArray();
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
    }
}
