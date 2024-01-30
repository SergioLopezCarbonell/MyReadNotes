package com.example.myapplication.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.adapters.LibraryBookAdapter;
import com.example.myapplication.classes.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LibraryFragment extends Fragment {

    private EditText searchBooksView;
    private Button buttonSearch;
    private LibraryBookAdapter libraryBookAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        new SearchBooksTask().execute("juego de ");
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        searchBooksView = view.findViewById(R.id.searchBooksView);
        buttonSearch = view.findViewById(R.id.buttonSearch);

        libraryBookAdapter = new LibraryBookAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(libraryBookAdapter);

        libraryBookAdapter.setOnItemClickListener(new LibraryBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                LibraryBookSelectedFragment selectedFragment = new LibraryBookSelectedFragment(book);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, selectedFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchBooksView.getText().toString();
                if (!query.isEmpty()) {
                    new SearchBooksTask().execute(query);
                }
            }
        });

        return view;
    }

    private class SearchBooksTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String query = params[0];
            try {
                URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(query, "UTF-8"));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = urlConnection.getInputStream();

                    return readStream(in);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                ArrayList<Book> searchResults = parseJsonResult(result);
                libraryBookAdapter.setBooks(searchResults);
            } else {
                // Handle lack of connection or errors
            }
        }
    }

    private String readStream(InputStream is) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            reader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<Book> parseJsonResult(String jsonResult) {
        ArrayList<Book> searchResults = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResult);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject bookObject = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");

                String imageUrl = "";
                if (volumeInfo.has("imageLinks")) {
                    imageUrl = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                }

                String title = volumeInfo.getString("title");
                String author = volumeInfo.getJSONArray("authors").getString(0);

                imageUrl = imageUrl.replace("http://", "https://");

                Book book = new Book(title, author, imageUrl, "Unknown Genre");

                book.setDescription(volumeInfo.optString("description", "No description available"));

                searchResults.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return searchResults;
    }
}
