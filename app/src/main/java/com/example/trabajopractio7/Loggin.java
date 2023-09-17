package com.example.trabajopractio7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.Manifest;

import com.example.trabajopractio7.databinding.ActivityLogginBinding;


public class Loggin extends AppCompatActivity {
    private ActivityLogginBinding binding;
    private LoginViewModel vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        vm= ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginViewModel.class);
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.login(binding.etUsurio.getText().toString(), binding.etPassword.getText().toString());
            }
        });
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},1000);
        }

        vm.getMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvMensaje.setText(s);
            }
        });
        registerReceiver(new Dialogo(), new IntentFilter("EXIT_APP"));
    }
}