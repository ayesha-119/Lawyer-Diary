package com.uptree.lawdiary.RoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    public void addBookmarkData(BookmarkList bookmarkList);

    @Query("select * from bookmarkList")
    public List<BookmarkList> getBookmarkData();

    @Query("SELECT EXISTS (SELECT 1 FROM bookmarkList WHERE id=:id)")
    public int isBookmark(int id);

    @Delete
    public void deleteBookmark(BookmarkList bookmarkList);

    @Query("DELETE FROM bookmarkList WHERE id = :id")
    public void deleteById(int id);
}
