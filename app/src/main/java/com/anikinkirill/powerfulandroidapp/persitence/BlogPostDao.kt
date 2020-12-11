package com.anikinkirill.powerfulandroidapp.persitence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anikinkirill.powerfulandroidapp.models.BlogPost

@Dao
interface BlogPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blogPost: BlogPost): Long

}