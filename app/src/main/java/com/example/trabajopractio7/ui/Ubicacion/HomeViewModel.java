package com.example.trabajopractio7.ui.Ubicacion;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
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

public class HomeViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<MapaActual> mMapa;
    private Location locActual;
    private MutableLiveData<Location> mLocation;
    private FusedLocationProviderClient fused;

    private static final LatLng MERLO= new LatLng(-32.320094, -65.0157063);
    private static final LatLng SANLUIS=new LatLng(-33.280576,-66.332482);
    private static final LatLng ULP=new LatLng(-33.150720,-66.306864);

    public HomeViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        fused = LocationServices.getFusedLocationProviderClient(context);

    }

    public LiveData<Location> getMLocation() {
        if (mLocation == null) {
            mLocation = new MutableLiveData<>();
        }
        return mLocation;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void obtenerUltimaUbicacion() {
        // promesa de localizacion
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //ActivityCompat.requestPermissions(getApplication(), new String[]{ACCESS_FINE_LOCATION}, 1000);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> tarea = fused.getLastLocation();
        //para obtener el actuvuty
        tarea.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //el postValue espera que llegue el dato
                if (location != null) {
                    mLocation.postValue(location);
                    locActual = location;

                }
            }
        });
    }

    public LiveData<MapaActual> getMMapa() {
        if (mMapa == null) {
            mMapa = new MutableLiveData<>();
        }
        return mMapa;
    }

    public void obtenerMapa() {
        MapaActual ma = new MapaActual();
        mMapa.setValue(ma);
    }


    public class MapaActual implements OnMapReadyCallback {
        private LatLng Actual;

        @RequiresApi(api = Build.VERSION_CODES.P)
        public void onMapReady(@NonNull GoogleMap googleMap) {
            GoogleMap mapa = googleMap;
            mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            obtenerUltimaUbicacion();

            // Obtener la ubicación actual
            /*if (mLocation.getValue() != null) {
                LatLng Actual = new LatLng(mLocation.getValue().getLatitude(),mLocation.getValue().getLongitude());

            } else {

                Actual = new LatLng(-33.280576, -66.332482);
            }*/
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mapa.setMyLocationEnabled(true);
            //agregue mas marcadores para que sea mas facil encontrar la ubicacion
            googleMap.addMarker(new MarkerOptions().position(SANLUIS).title("San Luis"));
            googleMap.addMarker(new MarkerOptions().position(ULP).title("ULP"));
            googleMap.addMarker(new MarkerOptions().position(MERLO).title("Merlo"));
                // Mover la cámara a la ubicación actual
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(SANLUIS)
                        .zoom(8)
                        .bearing(45) // Inclinación
                        .tilt(70)
                        .build();
                CameraUpdate update = CameraUpdateFactory.newCameraPosition(camPos);
                mapa.animateCamera(update);


        }}

}