package com.example.riiss.viktapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class Bildbibeliotek extends Activity {


    private TextView textviewn;
    Button button;
    ImageView imageView;
    static final int CAM_REQUEST=1;
    Date date=new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildbibeliotek);
        checkCameraHardware(this);

        button=(Button)findViewById(R.id.button3);
        imageView=(ImageView)findViewById(R.id.picture);


        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent camera_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file=getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent,CAM_REQUEST);

            }

        });


    }

    private File getFile(){

        File folder= new File("sdcard/viktapp_bilder");

        if(!folder.exists()){

            folder.mkdir();
        }
        File image=new File(folder,"cam_image.jpg");
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String path="sdcard/viktapp_bilder/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));

    }

    public void grafsida(View v){

        Intent intent= new Intent(this,Grafsidan.class);
        startActivity(intent);
    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            textviewn=(TextView)findViewById(R.id.textViewbottom);
            textviewn.setTextColor(Color.GREEN);
            textviewn.setText("Camera: V");
            return true;
        } else {
            // no camera on this device
            textviewn=(TextView)findViewById(R.id.textViewbottom);
            textviewn.setTextColor(Color.RED);
            textviewn.setText("Camera: X");
            return false;
        }
    }




}
