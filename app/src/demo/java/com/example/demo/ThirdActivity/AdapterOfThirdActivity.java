package com.example.demo.ThirdActivity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bcsquiz.R;
import com.example.bcsquiz.model.Users;

import java.util.List;

public class AdapterOfThirdActivity extends RecyclerView.Adapter<AdapterOfThirdActivity.ViewHolder> {

    private List<Users> usersList;

    public AdapterOfThirdActivity(List<Users> usersList) {
        this.usersList = usersList;
    }

    public void updateList (List<Users> usersList){
        this.usersList.addAll(usersList);
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(usersList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtName, txtPoints, num;
        public int position;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.put_name);
            txtPoints = itemView.findViewById(R.id.put_points);
            num = itemView.findViewById(R.id.numeration);
        }
        @SuppressLint("SetTextI18n")
        public void bind (Users user, int position) {
            if (user != null) {
                this.position = position;
                txtName.setText(user.getUser());
                txtPoints.setText(String.valueOf(user.getPoints()) + " points");
                num.setText(String.valueOf(getAdapterPosition()));
            }
        }
    }
}
