package com.mountreachsolution.qcut.ADAPTER;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mountreachsolution.qcut.CategorywiseSalon;
import com.mountreachsolution.qcut.POJO.PojoHomeCategory;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.common.url;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterHomePageTopProduct extends RecyclerView.Adapter<AdapterHomePageTopProduct.ViewHolder> {
    List<PojoHomeCategory> pojoHomeCategories;
    Activity activity;

    public AdapterHomePageTopProduct(List<PojoHomeCategory> pojoHomeCategories, Activity activity) {
        this.pojoHomeCategories = pojoHomeCategories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterHomePageTopProduct.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.home_page_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHomePageTopProduct.ViewHolder holder, int position) {
        PojoHomeCategory obj = pojoHomeCategories.get(position);
        holder.tvname.setText(obj.getCategoryName());
        Glide.with(activity)
                .load(url.address + "image/"+obj.getCategoryImage())
                .skipMemoryCache(true)
                .error(R.drawable.baseline_no_photography_24)// Resize the image to 800x800 pixels
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // caches original + resized
                .skipMemoryCache(false)
                .into(holder.cvImage);

        holder.cvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, CategorywiseSalon.class);
                i.putExtra("category",obj.getCategoryName());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pojoHomeCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvname;
        CircleImageView cvImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvImage = itemView.findViewById(R.id.civrvUserHomeFragmentCategoryImage);
            tvname = itemView.findViewById(R.id.tvrvUserHomeFragmentCategoryname);
        }
    }
}
