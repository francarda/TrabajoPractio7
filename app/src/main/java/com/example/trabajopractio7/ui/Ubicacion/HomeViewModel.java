package com.example.trabajopractio7.ui.Ubicacion;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.Closeable;

public class HomeViewModel extends ViewModel {
    private Context context;
    private MutableLiveData<MapaActual> mMapa;
    private MutableLiveData<Location> mLocation;
    private FusedLocationProviderClient fused;

    public HomeViewModel(@NonNull Application application) {
        super((Closeable) application);
        context = application.getApplicationContext();
        fused = LocationServices.getFusedLocationProviderClient(context);
    }

    public LiveData<Location> getMLocation() {
        if (mLocation == null) {
            mLocation = new MutableLiveData<>();
        }
        return mLocation;
    }

    public void obtenerUltimaUbicacion() {
        // promesa de localizacion
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> tarea = fused.getLastLocation();
        //para obtener el actuvuty
        tarea.addOnSuccessListener(context.getMainExecutor(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //el postValue espera que llegue el dato
                if (location != null) {
                    mLocation.postValue(location);
                }
            }
        });
    }

    public void lecturaPermanente() {
        LocationRequest request = LocationRequest.create();
        request.setInterval(5000);
        request.setFastestInterval(5000);
        request.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null) {

                    Location location = locationResult.getLastLocation();
                    mLocation.postValue(location);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fused.requestLocationUpdates(request, callback, null);
    }
    public LiveData<MapaActual> getMMapa(){
        if(mMapa==null){
            mMapa= new MutableLiveData<>();
        }
        return mMapa;
    }

    public void obtenerMapa(){
        MapaActual ma= new MapaActual();
        mMapa.setValue(ma);
    }


    public class MapaActual implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            Location loc= getMLocation().getValue();
            LatLng actual= new LatLng(loc.getLatitude(), loc.getLongitude());
            //Marcadores

            googleMap.addMarker(new MarkerOptions().position(actual).title("Ubicacion Actual"));

            //efecto de camara
            CameraPosition camPos= new CameraPosition.Builder()
                    .target(actual)
                    .zoom(19)
                    .bearing(45)//inclinacion
                    .tilt(70)
                    .build();
            CameraUpdate update= CameraUpdateFactory.newCameraPosition(camPos);
            googleMap.animateCamera(update);
        }
    }

}