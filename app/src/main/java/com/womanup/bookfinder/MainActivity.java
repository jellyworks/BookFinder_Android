package com.womanup.bookfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "KAKAO APIKEY";
    private static final String END_POINT = "https://dapi.kakao.com/v3/search/book?query=%s&page=%d";
    int page = 1;
    SearchView searchView;
    Button btnPrev;
    Button btnNext;
    RecyclerView recyclerView;
    BookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                BookThreadTask task = new BookThreadTask();
                task.execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        btnNext = findViewById(R.id.btn_next);
        btnPrev = findViewById(R.id.btn_prev);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.setEnabled(true);
                btnPrev.setEnabled(true);
                if(view.getId() == R.id.btn_next){
                    page++;
                } else {
                    page--;
                    if(page==1){
                        btnPrev.setEnabled(false);
                    }
                }
                BookThreadTask task = new BookThreadTask();
                task.execute(searchView.getQuery().toString());
            }
        };
        btnPrev.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);
        btnPrev.setEnabled(false);

        recyclerView = findViewById(R.id.recycler);
        adapter = new BookAdapter(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    String search(String query)  {
        String result="";
        String strURL = String.format(END_POINT, query, page);
        String str;
        try {
            URL url = new URL(strURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK "+ API_KEY);
            conn.connect();
            if(conn.getResponseCode()==conn.HTTP_OK){
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);

                StringBuffer buffer = new StringBuffer();
                while((str = reader.readLine()) != null){
                    buffer.append(str);
                }
                result = buffer.toString();
            }
        } catch (IOException e){
            System.out.println("예외발생");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public void parsing(String json){
        adapter.clear();
        try {
            JSONObject root = new JSONObject(json);
            JSONObject meta = root.getJSONObject("meta");
            boolean isEnd = meta.getBoolean("is_end");
            btnNext.setEnabled(!isEnd);
            JSONArray books = root.getJSONArray("documents");
            for(int i=0;i<books.length();i++) {
                JSONObject book = books.getJSONObject(i);
                String title = book.getString("title");
                String publisher = book.getString("publisher");
                int price = book.getInt("price");
                int salePrice = book.getInt("sale_price");
                String thumbnail = book.getString("thumbnail");
                String url = book.getString("url");
                JSONArray authors = book.getJSONArray("authors");
                String strAuthor = "";
                for (int j = 0; j < authors.length(); j++) {
                    if (j > 0) strAuthor = strAuthor + ",";
                    strAuthor = strAuthor + authors.getString(j);
                }
                JSONArray translators = book.getJSONArray("translators");
                String strTrans = "";
                for (int j = 0; j < translators.length(); j++) {
                    if (j > 0) strTrans = strTrans + ",";
                    strTrans = strTrans + translators.getString(j);
                }
                Book item = new Book(title, strAuthor, publisher,
                        price, salePrice, strTrans, thumbnail, url);
                adapter.add(item);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    class BookThreadTask extends ThreadTask<String, String>{
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String arg) {
            String query = arg;
            String str = search(query);
            System.out.println(str);
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            parsing(result);
        }
    }
}