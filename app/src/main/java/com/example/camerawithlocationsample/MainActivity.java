package com.example.camerawithlocationsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.camera2.Camera2AppConfig;
import androidx.camera.core.CameraX;
import androidx.fragment.app.Fragment;

import android.content.ContentProvider;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new MainFragment());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout,fragment).addToBackStack(fragment.toString()).commit();
    }

}
