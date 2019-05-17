package com.code.publicando.publicando.clases;

import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.code.publicando.publicando.R;
import com.code.publicando.publicando.fragments.ServiceListFragment;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private Context mCtx;
    private List<Servicios> serviciosList;
    private ImageView favo;
    private Integer mIdUser;
    private String idPost;

    //getting the context and product list with constructor
    public ListAdapter(Context mCtx, List<Servicios> serviciosList, Integer iduser) {
        this.mCtx = mCtx;
        this.serviciosList = serviciosList;
        this.mIdUser = iduser;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_servicioslist, null);
        CardView card_view =  (CardView) view.findViewById(R.id.card_viewlist);


        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        Servicios servicio = serviciosList.get(position);
        holder.cardView.setTag(position);

        holder.textViewTitle.setText(servicio.getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                String detalle = String.valueOf(serviciosList.get(position).getTitle());
                Intent myIntent = new Intent(mCtx, PostUploadPhotoActivity .class);
                myIntent.addFlags(FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                myIntent.putExtra("Detail", detalle);
                myIntent.putExtra("Type", "Servicio");
                myIntent.putExtra("idUser", mIdUser);
                mCtx.startActivity(myIntent);
                ((Activity) mCtx).finish();
                ((Activity) mCtx).overridePendingTransition(R.anim.fadein,R.anim.fadeout);*/

                //(Activity) mCtx).getFragmentManager().beginTransaction().
               //         remove((Activity) mCtx).getFragmentManager().findFragmentById(R.id.content_frame)).commit();

                String detalle = String.valueOf(serviciosList.get(position).getTitle());
                Fragment fragment = new ServiceListFragment();
                Bundle args = new Bundle();
                args.putString("Detail", detalle);
                args.putInt("idUser", mIdUser);
                fragment.setArguments(args);

                ((Activity) mCtx).getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return serviciosList.size();
    }


    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        ImageView imgFavorite;
        CardView cardView;
        public ListViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_viewlist);
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

}