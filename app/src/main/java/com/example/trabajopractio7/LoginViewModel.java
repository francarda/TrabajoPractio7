package com.example.trabajopractio7;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel {
private Context context;
private MutableLiveData<String> mensaje;
public LoginViewModel(@NonNull Application application) {
        super(application);
        context= application.getApplicationContext();
        }
public LiveData<String> getMensaje(){
        if(mensaje==null){
        mensaje= new MutableLiveData<>();

        }
        return mensaje;
        }
@SuppressLint("ResourceType")
public void login(String usu, String pass){
        if(usu.equals("usuario") && pass.equals("123")){
        Intent intent= new Intent(context, MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        mensaje.setValue("Acceso válido");
        }
        else{

        mensaje.setValue("Usuario y/o contraseña incorrecto");
        }
        }
        }
