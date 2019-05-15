package com.code.publicando.publicando.clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.activitys.PostSetDetailActivity;
import com.code.publicando.publicando.activitys.PostUploadPhotoActivity;
import com.code.publicando.publicando.fragments.ServiceDetailFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;

/**
 * Created by Belal on 10/18/2017.
 */


public class ServiciosAdapter extends RecyclerView.Adapter<ServiciosAdapter.ServiciosViewHolder> implements  View.OnClickListener {

    private Context mCtx;
    private List<Servicios> serviciosList;
    private ImageView favo;
    private Integer mIdUser;
    private String idPost;

    //getting the context and product list with constructor
    public ServiciosAdapter(Context mCtx, List<Servicios> serviciosList, Integer iduser) {
        this.mCtx = mCtx;
        this.serviciosList = serviciosList;
        this.mIdUser = iduser;

    }

    @Override
    public ServiciosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_servicios, null);
        CardView card_view =  (CardView) view.findViewById(R.id.card_view);
        card_view.setOnClickListener(this);

        return new ServiciosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ServiciosViewHolder holder, final int position) {

        Servicios servicio = serviciosList.get(position);
        holder.cardView.setTag(position);

        holder.textViewTitle.setText(servicio.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                idPost = String.valueOf(serviciosList.get(position).getId());
                Fragment fragment = new ServiceDetailFragment();
                Bundle args = new Bundle();
                args.putString("idPost", idPost);
                args.putInt("idUser", mIdUser);
                fragment.setArguments(args);
                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit();*/
                String detalle = String.valueOf(serviciosList.get(position).getTitle());
                Intent myIntent = new Intent(mCtx, PostUploadPhotoActivity .class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("Detail", detalle);
                myIntent.putExtra("Type", "Servicio");
                myIntent.putExtra("idUser", mIdUser);
                mCtx.startActivity(myIntent);
                ((Activity) mCtx).finish();
                ((Activity) mCtx).overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        });




        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(servicio.getImage()));
    }


    @Override
    public int getItemCount() {
        return serviciosList.size();
    }


    class ServiciosViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        ImageView imgFavorite;
        CardView cardView;
        public ServiciosViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            imageView = itemView.findViewById(R.id.imageView);
            imgFavorite = itemView.findViewById(R.id.favorite);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onClick(View v)
    {

        //int position = (int) v.getTag();
        int id = v.getId();

        switch (id) {
            case R.id.imageView:

/*            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);

            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View dialogView = inflater.inflate(R.layout.service_detail, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNegativeButton("Cerrar",null );
            ImageView editText = (ImageView) dialogView.findViewById(R.id.ImageDetail);
            ImageView imagen = v.findViewById(R.id.imageView);
            //editText.setImageResource(R.drawable.comidas);
            editText.setImageDrawable(imagen.getDrawable());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();*/
                LayoutInflater inflater = LayoutInflater.from(mCtx);
                Intent myIntent = new Intent(mCtx, PostUploadPhotoActivity.class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("Detail", "test");
                myIntent.putExtra("Type", "test");
                myIntent.putExtra("idUser", mIdUser);
                mCtx.startActivity(myIntent);

            break;
        }
    }

}
