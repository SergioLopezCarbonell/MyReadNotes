package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.adapters.BookAdapter;
import com.example.myapplication.R;
import com.example.myapplication.dao.PersonalBookDao;
import com.example.myapplication.entitys.PersonalBookEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<PersonalBookEntity> books;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_books, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);


        PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();


        books = new ArrayList<PersonalBookEntity>();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                books = personalBookDao.getAllPersonalBooks();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recyclerView.getAdapter() == null) {

                            bookAdapter = new BookAdapter(getContext(), books);
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                            recyclerView.setAdapter(bookAdapter);

                            bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(PersonalBookEntity book) {
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container, new BookSelectedFragment(book));
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });


                            bookAdapter.setOnItemLongClickListener(new BookAdapter.OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(String coverUrl) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.TransparentDialog);
                                    View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_large_cover, null);

                                    ImageView imageView = dialogView.findViewById(R.id.dialogImageView);
                                    Picasso.get().load(coverUrl).into(imageView);

                                    builder.setView(dialogView);


                                    int desiredWidth = 500;
                                    int desiredHeight = 800;

                                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                                    layoutParams.width = desiredWidth;
                                    layoutParams.height = desiredHeight;
                                    imageView.setLayoutParams(layoutParams);

                                    builder.show();
                                }
                            });
                        }
                        else{
                            bookAdapter.updateData(books);
                        }

                    }
                });
            }
        });

        Button buttonAll = view.findViewById(R.id.button);
        Button buttonFiction = view.findViewById(R.id.button1);
        Button buttonNonFiction = view.findViewById(R.id.button2);
        Button buttonLiteraryGenre = view.findViewById(R.id.button3);
        Button buttonAdventures = view.findViewById(R.id.button4);
        Button buttonMistery = view.findViewById(R.id.button5);
        Button buttonRomance = view.findViewById(R.id.button6);
        Button buttonTerror = view.findViewById(R.id.button7);
        Button buttonOthers = view.findViewById(R.id.button8);


        buttonAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("All");
            }
        });
        buttonFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Fiction");
            }
        });
        buttonNonFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Non-Fiction");
            }
        });
        buttonLiteraryGenre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Literary Genre");
            }
        });
        buttonAdventures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Adventures");
            }
        });
        buttonMistery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Mistery");
            }
        });
        buttonRomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Romance");
            }
        });
        buttonTerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBooksByCategory("Terror");
            }
        });
        buttonOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterBooksByCategory("Others");
            }
        });

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooksBySearch(newText);
                return true;
            }
        });






        return view;
    }

    private void filterBooksByCategory(String category) {
        PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();
        Log.d("Filter", "Filtering books by category: " + category);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                if(category.equals("All")){
                    List<PersonalBookEntity> filteredBooks = personalBookDao.getAllPersonalBooks();
                    Log.d("Filter", "Filtered books count: " + filteredBooks.size());
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bookAdapter != null) {
                                bookAdapter.updateData(filteredBooks);
                            }
                        }
                    });
                }
                else{
                    List<PersonalBookEntity> filteredBooks = personalBookDao.getPersonalBooksByCategory(category);
                    Log.d("Filter", "Filtered books count: " + filteredBooks.size());
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bookAdapter != null) {
                                bookAdapter.updateData(filteredBooks);
                            }
                        }
                    });
                }

            }
        });
    }

    private void filterBooksBySearch(String query) {
        PersonalBookDao personalBookDao = AppDatabase.getInstance(requireContext()).personalBookDao();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<PersonalBookEntity> filteredBooks = personalBookDao.getPersonalBooksBySearch(query);

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bookAdapter != null) {
                            bookAdapter.updateData(filteredBooks);
                        }
                    }
                });
            }
        });
    }


}