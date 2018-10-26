package com.code.publicando.publicando.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.code.publicando.publicando.ChooseZoneActivity;
import com.code.publicando.publicando.CreateAccountActivity;
import com.code.publicando.publicando.LocationActivity;
import com.code.publicando.publicando.LoginActivity;
import com.code.publicando.publicando.R;
import com.code.publicando.publicando.SetZoneActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseZoneFragment extends Fragment implements View.OnClickListener{

    private Context context;

    public ChooseZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_choose_zone, container, false);
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Provincia");
        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray2.add("Localidad");
        List<String> spinnerArray3 =  new ArrayList<String>();
        spinnerArray3.add("Barrio");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray3);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner sItems = (Spinner) view.findViewById(R.id.spinner);
        Spinner sItems2 = (Spinner) view.findViewById(R.id.spinner2);
        Spinner sItems3 = (Spinner) view.findViewById(R.id.spinner3);
        sItems.setAdapter(adapter);
        sItems2.setAdapter(adapter2);
        sItems3.setAdapter(adapter3);

        Button next = view.findViewById(R.id.nextFragment);
        next.setOnClickListener(this);

        Button myUbication = view.findViewById(R.id.myUbicationFragment);
        myUbication.setOnClickListener(this);

        SupportPlaceAutocompleteFragment  autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);
        if(autocompleteFragment==null){
            autocompleteFragment = (SupportPlaceAutocompleteFragment) SupportPlaceAutocompleteFragment.instantiate(context, "com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment");
            EditText etPlace = (EditText) view.findViewById(R.id.place_autocomplete_search_input);
            etPlace.setHint("Calle y Altura (Opcional)");
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                }

                @Override
                public void onError(Status status) {

                }
            });

           // fm.beginTransaction().replace(R.id.autocomplete_fragment, autocompleteFragment).commit();
        }
/*        autocompleteFragment.setHint("Calle y Altura (Opcional)");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(context, place.getName().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });*/
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent myIntent;
        switch (id)
        {
            case R.id.nextFragment:
                myIntent = new Intent(getActivity(), LocationActivity.class);
                getActivity().startActivity(myIntent);
                break;
            case R.id.myUbicationFragment:

                myIntent = new Intent(getActivity(), LocationActivity.class);
                getActivity().startActivity(myIntent);
                break;
        }
    }

}
