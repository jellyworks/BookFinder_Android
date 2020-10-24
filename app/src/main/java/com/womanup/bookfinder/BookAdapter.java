package com.womanup.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    Context context;
    ArrayList<Book> books;

    public BookAdapter(Context context) {
        this.context = context;
        books = new ArrayList<>();
    }

    public void add(Book book){
        books.add(book);
    }

    public void clear(){
        books.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.book_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);
        holder.textTitle.setText(book.title);
        holder.textAuthor.setText(book.author);
        holder.textPublisher.setText(book.publisher);
        holder.textPrice.setText(book.salePrice+"");
        Glide.with(holder.itemView.getContext()).load(book.thumbnail).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textTitle;
        TextView textAuthor;
        TextView textPublisher;
        TextView textPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_item);
            textTitle = itemView.findViewById(R.id.textTitle_item);
            textAuthor = itemView.findViewById(R.id.textAuthor_item);
            textPublisher = itemView.findViewById(R.id.text_publisher_item);
            textPrice = itemView.findViewById(R.id.text_price_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Book book = books.get(pos);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("book", book);
                    context.startActivity(intent);
                }
            });
        }
    }
}
