package com.code.publicando.publicando;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ServicesList_Activity extends AppCompatActivity {
    //a list to store all the products
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list_);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        //creating recyclerview adapter
        ProductAdapter adapter = new ProductAdapter(this, productList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}
