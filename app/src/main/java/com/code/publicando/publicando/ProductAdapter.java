package com.code.publicando.publicando;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements  View.OnClickListener {


    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_products, null);
        CardView card_view =  (CardView) view.findViewById(R.id.card_view);
        card_view.setOnClickListener(this);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getShortdesc());
        //holder.textViewRating.setText(String.valueOf(product.getRating()));
        //holder.textViewPrice.setText(String.valueOf(product.getPrice()));

        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage()));

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            //textViewRating = itemView.findViewById(R.id.textViewRating);
            //textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public void onClick(View v)
    {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);

            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View dialogView = inflater.inflate(R.layout.service_detail, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setNegativeButton("Cerrar",null );
            ImageView editText = (ImageView) dialogView.findViewById(R.id.ImageDetail);
            ImageView imagen = v.findViewById(v.getId());
            //editText.setImageResource(R.drawable.comidas);
            editText.setImageDrawable(imagen.getDrawable());
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
    }
}
