package com.mountreachsolution.qcutadmin.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.qcutadmin.R;
import com.mountreachsolution.qcutadmin.User;
import com.mountreachsolution.qcutadmin.UserDetails;
import com.mountreachsolution.qcutadmin.comman.url;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {

    private Context context;
    private List<User> userList;
    private List<User> userListFull; // backup list for filtering

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.userListFull = new ArrayList<>(userList); // keep original copy
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.name.setText(user.getName());
        holder.address.setText(user.getAddress());
        holder.email.setText(user.getEmail());
        holder.mobile.setText(user.getMobile());
        holder.role.setText(user.getUserrole());

        Glide.with(context)
                .load(url.imageaddress + user.getImage()) // image URL from server
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, UserDetails.class);
                i.putExtra("name",user.getName());
                i.putExtra("email",user.getEmail());
                i.putExtra("mobile",user.getMobile());
                i.putExtra("userrole",user.getUserrole());
                i.putExtra("address",user.getAddress());
                i.putExtra("image",user.getImage());
                i.putExtra("id",user.getId());
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name, address, email, mobile, role;
        Button btnRemove;


        CardView cardView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.userImage);
            name = itemView.findViewById(R.id.userName);
            address = itemView.findViewById(R.id.userAddress);
            email = itemView.findViewById(R.id.userEmail);
            mobile = itemView.findViewById(R.id.userMobile);
            role = itemView.findViewById(R.id.userRole);
            cardView = itemView.findViewById(R.id.card);

        }
    }

    // ✅ Implement search filter
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private final Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(userListFull); // show all if empty search
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User user : userListFull) {
                    if (user.getName().toLowerCase().contains(filterPattern) ||
                            user.getEmail().toLowerCase().contains(filterPattern) ||
                            user.getMobile().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((List<User>) results.values);
            notifyDataSetChanged();
        }
    };
}
