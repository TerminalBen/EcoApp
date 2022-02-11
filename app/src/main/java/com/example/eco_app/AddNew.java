package com.example.eco_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class AddNew extends AppCompatActivity {

    private Toolbar mtoolbar;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    ImageView camera;
    Uri image_uri;
    EditText latitudeText;
    EditText longitudeText;
    EditText name;
    EditText link;
    Data data;
    private double Latitude;
    private double Longitude;
    private Dialog choce_dialog;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button locate_btn;
    ImageView save;
    private FirebaseStorage storage;
    private StorageReference StorageRef;
    DatabaseReference dbref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);


        mtoolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar = findViewById(R.id.toolbar_add);
        setSupportActionBar(mtoolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final String[] latStr = new String[1];
        final String[] longStr = new String[1];

        camera = findViewById(R.id.camera_button);
        locate_btn = findViewById(R.id.button2);
        latitudeText = findViewById(R.id.lat);
        longitudeText = findViewById(R.id.longi);
        link = findViewById(R.id.speciesLink);
        name = findViewById(R.id.speciesName);
        data = new Data();
        dbref = FirebaseDatabase.getInstance().getReference().child("Data");
        save = findViewById(R.id.save_button);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        storage = FirebaseStorage.getInstance();
        StorageRef  = storage.getReference();


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choce_dialog.show();
            }
        });

        locate_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},44);
                }

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("Location: ",String.valueOf(location));
                // now get the lat/lon from the location and do something with it.
                setLocationText(location.getLatitude(), location.getLongitude());
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                String latStr = Double.toString(Latitude);
                String longStr = Double.toString(Longitude);
                Log.d("Latitude: ", latStr);
                Log.d("Longitude: ", longStr);
                setLocationText(Latitude,Longitude);
            }
        });
        choce_dialog = new Dialog( AddNew.this);
        choce_dialog.setContentView(R.layout.photo_method);
        choce_dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        choce_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        choce_dialog.setCancelable(true);

        Button from_camera = (Button) choce_dialog.findViewById(R.id.use_camera);
        Button from_gallery = (Button) choce_dialog.findViewById(R.id.use_galery);
        from_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useCamera();
                choce_dialog.hide();
            }
        });

        from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetcontent.launch("image/*");
                choce_dialog.hide();
            }
        });
        /*
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                    }else {
                        Log.d("Uploading: ","onClick");
                        Toast.makeText(getApplicationContext(), "ũploading...", Toast.LENGTH_SHORT).show();
                        //mProgressDialog.setMessage("Uploading Message");
                        //mProgressDialog.show();
                        //upload();
                    }
                }
            }
        });

         */
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


    }

    ActivityResultLauncher<String> mGetcontent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if(result != null){
                        camera.setImageURI(result);
                    }
                }
            });

    private void upload(){
        final ProgressDialog pd = new ProgressDialog(this);
        String imageID;
        imageID = System.currentTimeMillis()+"."+image_uri;
        data.setName(name.getText().toString().trim());
        data.setLink(link.getText().toString().trim());

        data.setImgId(imageID);

        double lat = Double.parseDouble(latitudeText.getText().toString().trim());
        data.setLatitude(lat);

        double lon = Double.parseDouble(longitudeText.getText().toString().trim());
        data.setLatitude(lon);

        dbref.push().setValue(data);

        pd.setTitle("---Uploading---");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        // Create a reference to "mountains.jpg"

        StorageReference reference = StorageRef.child("Imagens/"+randomKey);

        reference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(R.id.content),"image Uploaded",Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                        pd.setMessage("progress"+(int)progressPercent + "%");
                    }
                });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void useGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    public void useCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                // permission already granted
                openCamera();
            }
        } else {
            // system < marshmellow
            openCamera();

        }
    }


    private void setLocationText(Double a,Double b){
        latitudeText.setText(Double.toString(a));
        longitudeText.setText(Double.toString(b));
    }



    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        // openCamera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.secondary_menu,menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // this method is called when user accepts or denies permission from the pop up request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        //called when image was captured from camera
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            // set our image captured to our image view
            camera.setImageURI(image_uri);
            //upload();
        }

    }


}