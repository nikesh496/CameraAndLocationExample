package com.example.camerawithlocationsample;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.net.Uri;

import java.io.File;
import java.util.concurrent.Executor;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {


    private static final int CAMERA_REQUEST = 1888;
    private Size mCameraSize;
    private MainActivity mActivity;
    private Executor mCameraDeviceCallback;
    private CameraCharacteristics mCameraCharacteristics;
    private Button openCameraBTN;
    private static final int cameraRequestCode = 2000;
    private ImageView imageViewIV;
    private Uri mImageUri;
    public static Uri imageFinalUri = null;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = (MainActivity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        openCameraBTN = view.findViewById(R.id.openCameraBTN);
        imageViewIV = view.findViewById(R.id.imageViewIV);
        imageViewIV.setVisibility(View.GONE);


        openCameraBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.openCameraBTN:
                openCamera();
                break;
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try
        {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        }
        catch(Exception e)
        {
            Log.v("openCamera()", "Can't create file to take picture!");
            Toast.makeText(mActivity, "Please check SD card! Image shot is impossible!", 10000);
            return ;
        }
        mImageUri = FileProvider.getUriForFile(mActivity, mActivity.getApplicationContext().getPackageName() + ".fileprovider", photo);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewIV.setVisibility(View.VISIBLE);
            imageViewIV.setImageBitmap(photo);
        }
    }*/

    public void grabImage(ImageView imageView)
    {
        boolean uriSuccess = false;
        getActivity().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = mActivity.getContentResolver();
        Bitmap bitmap;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            imageView.setImageBitmap(bitmap);
            uriSuccess = true;
        }
        catch (Exception e)
        {
            Toast.makeText(mActivity, "Failed to load", Toast.LENGTH_SHORT).show();
            Log.d("grabImage()", "Failed to load", e);
        }finally {
            if(uriSuccess){
                imageFinalUri = mImageUri;
            }
        }
    }


    //called after camera intent finished
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        //MenuShootImage is user defined menu option to shoot image
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK)
        {
            ImageView imageView;
            //... some code to inflate/create/find appropriate ImageView to place grabbed image
            imageView = getView().findViewById(R.id.imageViewIV);
            imageView.setVisibility(View.VISIBLE);
            this.grabImage(imageView);
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
