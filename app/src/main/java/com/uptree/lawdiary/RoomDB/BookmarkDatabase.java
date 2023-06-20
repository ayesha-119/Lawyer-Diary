package com.uptree.lawdiary.RoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={BookmarkList.class},version = 4)
public abstract class BookmarkDatabase extends RoomDatabase{
    public abstract BookmarkDao bookmarkDao();
}
