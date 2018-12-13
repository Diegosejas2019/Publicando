package com.code.publicando.publicando.clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.code.publicando.publicando.R;
import com.code.publicando.publicando.fragments.ServiceDetailFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements  View.OnClickListener {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;
    private ImageView favo;
    private Integer mIdUser;
    private String idPost;
    private static final String TAG_SUCCESS = "StatusCode";
    //the recyclerview
    RecyclerView recyclerView;
    JSONArray jsonarray;
    JSONObject jsonobject;
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList,Integer iduser) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.mIdUser = iduser;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        CardView card_view =  (CardView) view.findViewById(R.id.card_view);
        //        //card_view.setOnClickListener(this);
        ImageView imageView =  (ImageView) view.findViewById(R.id.imageView);
        //imageView.setOnClickListener(this);
        favo =  (ImageView) view.findViewById(R.id.favorite);
        //favorite.setOnClickListener(this);
        TextView textViewTitle =  (TextView) view.findViewById(R.id.textViewTitle);
        //textViewTitle.setOnClickListener(this);
        TextView textViewShortDesc =  (TextView) view.findViewById(R.id.textViewShortDesc);
        //textViewShortDesc.setOnClickListener(this);
        RatingBar ratingBar =  (RatingBar) view.findViewById(R.id.ratingBar);
        //ratingBar.setOnClickListener(this);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        //getting the product of the specified position
        Product product = productList.get(position);
        holder.cardView.setTag(position);
        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPost = String.valueOf(productList.get(position).getId());
                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .remove(((Activity) mCtx).getFragmentManager().findFragmentById(R.id.content_frame)).commit();
                Fragment fragment = new ServiceDetailFragment();
                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        holder.textViewShortDesc.setText(product.getShortdesc());
        holder.textViewShortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idPost = String.valueOf(productList.get(position).getId());
                Fragment fragment = new ServiceDetailFragment();
                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        });
        //holder.textViewRating.setText(String.valueOf(product.getRating()));
        //holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        if(productList.get(position).getFavorite() == 1)
        {
            holder.imgFavorite.setTag(R.drawable.ic_favorite_selected);
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
        }
        else
        {
            holder.imgFavorite.setTag(R.drawable.ic_favorite_unselected);
            holder.imgFavorite.setImageResource(R.drawable.ic_favorite_unselected);
        }
        //holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));
        Url url = new Url();
        Picasso.with(mCtx)
                .load(url.getDireccion() + "/Imagenes/" + product.getImageUrl().substring((product.getImageUrl().length()-6)).replaceAll("\\\\", ""))
                .resize(1400, 850)
                .into(holder.imageView);

        holder.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idPost = String.valueOf(productList.get(position).getId());
                //Toast.makeText(mCtx, String.valueOf(productList.get(position).getId()) , Toast.LENGTH_SHORT).show();
                //Toast.makeText(mCtx,String.valueOf(mIdUser),Toast.LENGTH_LONG).show();
                if ( holder.imgFavorite.getTag().equals(R.drawable.ic_favorite_selected) )
                {
                    holder.imgFavorite.setImageResource(R.drawable.ic_favorite_unselected);
                    holder.imgFavorite.setTag(R.drawable.ic_favorite_unselected);
                }
                else{
                    holder.imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
                    holder.imgFavorite.setTag(R.drawable.ic_favorite_selected);
                }

                new MarcarFavoritos().execute();
            }
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        ImageView imgFavorite;
        CardView cardView;
        public ProductViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            //textViewRating = itemView.findViewById(R.id.textViewRating);
            //textViewPrice = itemView.findViewById(R.id.textViewPrice);
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

/*        //int position = (int) v.getTag();
        int id = v.getId();

        switch (id) {
            case R.id.favorite:
                Toast.makeText(mCtx,"algo",Toast.LENGTH_LONG).show();
                break
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);

            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View dialogView = inflater.inflate(R.layout.service_detail, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNegativeButton("Cerrar",null );
            ImageView editText = (ImageView) dialogView.findViewById(R.id.ImageDetail);
            ImageView imagen = v.findViewById(R.id.imageView);
            //editText.setImageResource(R.drawable.comidas);
            editText.setImageDrawable(imagen.getDrawable());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
          /*  break;
        }*/
    }

/*    public class ViewHolder extends RecyclerView.ViewHolder{

        //CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }*/

    public class MarcarFavoritos extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mCtx);
            pDialog.setMessage("Guardando Favorito...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            Boolean flag = false;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("IdUser", String.valueOf(mIdUser)));
            nameValuePairs.add(new BasicNameValuePair("IdPost", idPost));

            String Resultado="";
            Url url = new Url();
            JSONObject json = jParser.makeHttpRequest(url.getDireccion() + "/api/master/ChangeFavorite", "POST", nameValuePairs);

            try {
                if (json != null){
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 200){
                        flag = true;}
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Resultado = e.getMessage();
            }
            return flag;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            pDialog.dismiss();
            //mAuthTask = null;
            Toast.makeText(mCtx,"Sin conexión",Toast.LENGTH_LONG).show();
        }
    }
}
