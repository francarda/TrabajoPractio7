package com.example.trabajopractio7.ui.Ubicacion;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabajopractio7.MainActivity;
import com.example.trabajopractio7.R;
import com.example.trabajopractio7.databinding.FragmentHomeBinding;
import com.example.trabajopractio7.ui.MiMusica.GalleryViewModel;
import com.google.android.gms.maps.SupportMapFragment;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel mv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        mv = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        mv.getMLocation().observe(getActivity(), new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                binding.tvMostrar.setText(location.getLatitude()+" "+location.getLongitude());

            }
        });

        mv.getMMapa().observe(getActivity(), new Observer<HomeViewModel.MapaActual>() {
            @Override
            public void onChanged(HomeViewModel.MapaActual mapaActual) {
                //SupportMapFragment smf= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                SupportMapFragment smf= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                smf.getMapAsync(mapaActual);
            }
        });
        mv.obtenerMapa();



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}