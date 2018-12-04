package com.code.publicando.publicando.activitys;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

public class SetZoneActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_zone);

        Dots_Layout = (LinearLayout) findViewById(R.id.dotsLayout);
        createDots(1);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Provincia");
        List<String> spinnerArray2 =  new ArrayList<String>();
        spinnerArray2.add("Localidad");
        List<String> spinnerArray3 =  new ArrayList<String>();
        spinnerArray3.add("Barrio");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray3);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner sItems = (Spinner) findViewById(R.id.spinner);
        Spinner sItems2 = (Spinner) findViewById(R.id.spinner2);
        Spinner sItems3 = (Spinner) findViewById(R.id.spinner3);
        sItems.setAdapter(adapter);
        sItems2.setAdapter(adapter2);
        sItems3.setAdapter(adapter3);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Calle y Altura (Opcional)");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(SetZoneActivity.this, place.getName().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    private void createDots(int current_position)
    {
        if (Dots_Layout!=null)
            Dots_Layout.removeAllViews();

        dots = new ImageView[3];

        for (int i= 0; i < dots.length; i++)
        {
            dots[i] = new ImageView(this);
            if (i==current_position)
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.active_dots));
            }
            else
            {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.default_dots));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(4,0,4,0);

            Dots_Layout.addView(dots[i],params);
        }

    }

    @Override
    public void onClick(View view) {
        Intent myIntent = new Intent(SetZoneActivity.this,
                LocationActivity.class);
        startActivity(myIntent);
        SetZoneActivity.this.finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}
