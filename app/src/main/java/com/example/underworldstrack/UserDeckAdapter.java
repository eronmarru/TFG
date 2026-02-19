package com.example.underworldstrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserDeckAdapter extends RecyclerView.Adapter<UserDeckAdapter.UserDeckViewHolder> {

    private List<UserDeck> userDecks;
    private Context context;
    private DatabaseHelper dbHelper;

    public UserDeckAdapter(Context context, List<UserDeck> userDecks) {
        this.context = context;
        this.userDecks = userDecks;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public UserDeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_deck, parent, false);
        return new UserDeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDeckViewHolder holder, int position) {
        UserDeck deck = userDecks.get(position);
        holder.tvName.setText(deck.getName());

        holder.tvDetails.setText("ID: " + deck.getId());

        holder.itemView.setOnClickListener(v -> {
             Intent intent = new Intent(context, UserDeckDetailActivity.class);
             intent.putExtra("USER_DECK_ID", deck.getId());
             intent.putExtra("USER_DECK_NAME", deck.getName());
             context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userDecks.size();
    }

    public static class UserDeckViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;

        public UserDeckViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDeckName);
            tvDetails = itemView.findViewById(R.id.tvDeckDetails);
        }
    }
}