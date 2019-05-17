package com.code.publicando.publicando.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.activitys.MainActivity;
import com.code.publicando.publicando.clases.JSONParser;
import com.code.publicando.publicando.clases.ListAdapter;
import com.code.publicando.publicando.clases.Post;
import com.code.publicando.publicando.clases.Product;
import com.code.publicando.publicando.clases.Servicios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class filter extends Fragment {

    List<Product> productList;

    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private String Type;
    private Integer IdUser;
    ArrayList<Post> posts;
    private Context context;
    List<Servicios> serviciosList;
    public filter() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_filter, container, false);


        Bundle args = getArguments();
        Type = args.getString("Type", null);
        IdUser = args.getInt("IdUser", 0);

        ((MainActivity) getActivity())
                .setActionBarTitle(Type);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //initializing the productlist
        serviciosList = new ArrayList<>();


        //adding some items to our list
        serviciosList.add(
                new Servicios(
                        1,
                        "Abogado"));

        serviciosList.add(
                new Servicios(
                        2,
                        "Arquitecto"));

        serviciosList.add(
                new Servicios(
                        3,
                        "Electricista"));

        serviciosList.add(
                new Servicios(
                        4,
                        "Pintor"));

        serviciosList.add(
                new Servicios(
                        5,
                        "Gacista"));

        serviciosList.add(
                new Servicios(
                        6,
                        "Programador"));
        //creating recyclerview adapter
        ListAdapter adapterServicio = new ListAdapter(context, serviciosList,1);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapterServicio);

        return view;
    }

}
