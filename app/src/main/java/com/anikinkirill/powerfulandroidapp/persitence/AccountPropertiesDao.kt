package com.anikinkirill.powerfulandroidapp.persitence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.anikinkirill.powerfulandroidapp.models.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Insert(onConflict = REPLACE)
    fun insertAndReplace(accountProperties: AccountProperties) : Long

    @Insert(onConflict = IGNORE)
    fun insertOrIgnore(accountProperties: AccountProperties) : Long

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    fun searchByPk(pk: Int) : AccountProperties?

    @Query("SELECT * FROM account_properties WHERE email = :email")
    fun searchByEmail(email: String) : AccountProperties?

}