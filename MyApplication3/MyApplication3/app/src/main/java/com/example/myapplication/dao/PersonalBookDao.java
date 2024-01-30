package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entitys.PersonalBookEntity;

import java.util.List;

@Dao
public interface PersonalBookDao {

    @Insert
    long insertPersonalBook(PersonalBookEntity personalBook);

    @Query("SELECT * FROM personal_books")
    List<PersonalBookEntity> getAllPersonalBooks();

    @Query("DELETE FROM personal_books WHERE id = :personalBookId")
    void deletePersonalBookById(long personalBookId);

    @Query("SELECT COUNT(*) FROM personal_books")
    int getBookCount();
    @Query("SELECT COUNT(*) FROM personal_books WHERE title = :bookTitle AND author = :bookAuthor")
    int getBookCountByTitleAndAuthor(String bookTitle, String bookAuthor);

    @Query("SELECT COUNT(*) FROM personal_books WHERE finish = 1")
    int getFinishedBookCount();

    @Query("SELECT COUNT(*) FROM personal_books WHERE category = :bookCategory")
    int getBookCountByCategory(String bookCategory);
    @Update
    void updatePersonalBook(PersonalBookEntity personalBook);

    @Query("SELECT * FROM personal_books WHERE category = :category")
    List<PersonalBookEntity> getPersonalBooksByCategory(String category);

    @Query("SELECT * FROM personal_books WHERE title LIKE '%' || :query || '%'")
    List<PersonalBookEntity> getPersonalBooksBySearch(String query);



}
