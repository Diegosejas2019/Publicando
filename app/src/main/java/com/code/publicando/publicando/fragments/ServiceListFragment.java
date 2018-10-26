package com.code.publicando.publicando.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.code.publicando.publicando.Product;
import com.code.publicando.publicando.ProductAdapter;
import com.code.publicando.publicando.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceListFragment extends Fragment {

    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    private Context context;
    public ServiceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);

/*        Button btnZona = view.findViewById(R.id.btnZona);
        btnZona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new ChooseZoneFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.content_frame, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
            }
        });*/

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //initializing the productlist
        productList = new ArrayList<>();


        //adding some items to our list
        productList.add(
                new Product(
                        1,
                        "Comercio modelo 1",
                        "10 Sucursales en zona oeste",
                        R.drawable.comercio));

        productList.add(
                new Product(
                        1,
                        "Resto Modelo 1",
                        "Parrilla",
                        R.drawable.comidas));

        productList.add(
                new Product(
                        1,
                        "Promos Modelo 1",
                        "Increibles descuentos",
                        R.drawable.promos));

        productList.add(
                new Product(
                        1,
                        "Comercio modelo 1",
                        "10 Sucursales en zona oeste",
                        R.drawable.comercio));

        productList.add(
                new Product(
                        1,
                        "Resto Modelo 1",
                        "Parrilla",
                        R.drawable.comidas));

        productList.add(
                new Product(
                        1,
                        "Promos Modelo 1",
                        "Increibles descuentos",
                        R.drawable.promos));
        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(context, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onClickZona() {
        Toast.makeText(context,"algo",Toast.LENGTH_LONG).show();
    }
}
