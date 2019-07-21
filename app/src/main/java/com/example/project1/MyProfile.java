package com.example.project1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    ImageView backgroundImage;
    ImageView profileImage;
    TextView name;
    public Bitmap pf,cp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        backgroundImage=findViewById(R.id.imageView2);
        profileImage=findViewById(R.id.profile_image);
        name=findViewById(R.id.name11);

        name.setText(ParseUser.getCurrentUser().getUsername());
        profileImage.setOnClickListener(this);
        backgroundImage.setOnClickListener(this);

        updateProfilePic(ParseUser.getCurrentUser(),profileImage);

        updateCoverPic();


    }


    public void updateProfilePic(ParseUser user, final ImageView view){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ProfilePic");
        query.whereEqualTo("username", user.getUsername());
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

    public void updateCoverPic(){
        ParseQuery<ParseObject> query=ParseQuery.getQuery("CoverPic");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.orderByDescending("updatedAt");
        try {
            if(query.count() == 0){
                backgroundImage.setImageResource(R.drawable.b5);

            }
            else
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e==null){
                            if(objects!=null){
                                ParseFile file=objects.get(0).getParseFile("coverpic");
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if(e==null){
                                            if(data!=null){
                                                Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                                backgroundImage.setImageBitmap(bitmap);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==profileImage.getId()||v.getId()==backgroundImage.getId()){
            View k= v;
            Intent intent=new Intent(MyProfile.this,ViewPhoto.class);
            k.buildDrawingCache();
            Bitmap image=k.getDrawingCache();
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray=stream.toByteArray();
            intent.putExtra("image",byteArray);
            startActivity(intent);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else
                {
                    getPhoto(1);
                }
            }
            else{
                getPhoto(1);
            }
        }
        else if(item.getItemId()==R.id.item3){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else
            {
                getPhoto(2);
            }
        }
        else{
            getPhoto(2);
        }
        }
        else if(item.getItemId()==R.id.item1){
            ParseUser.logOut();
            finish();
        }
        else if(item.getItemId()==R.id.item4){
            startActivity(new Intent(MyProfile.this,PeopleYouMayKnow.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1||requestCode==2){
            if(grantResults!=null&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                getPhoto(requestCode);
            }
        }
    }

    public void getPhoto(int requestCode){
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1&&resultCode==RESULT_OK&&data!=null){
            Uri selectedImage=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImage.setImageBitmap(bitmap);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray=stream.toByteArray();
            ParseFile file=new ParseFile("image.png",byteArray);

            ParseObject object=new ParseObject("ProfilePic");
            object.put("username",ParseUser.getCurrentUser().getUsername());
            object.put("profilepic",file);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(MyProfile.this,"Image Recived",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else if(requestCode==2&&resultCode==RESULT_OK&&data!=null){
            Uri selectedImage=data.getData();
            Bitmap bitmap = null;
            try {
                bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            backgroundImage.setImageBitmap(bitmap);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] byteArray=stream.toByteArray();
            ParseFile file=new ParseFile("image.png",byteArray);

            ParseObject object=new ParseObject("CoverPic");
            object.put("username",ParseUser.getCurrentUser().getUsername());
            object.put("coverpic",file);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        Toast.makeText(MyProfile.this,"Image Recived",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MyProfile.this,"ERROR"+e.toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
}
