package com.example.rickmortyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rickmortyapp.data.local.dao.CharacterDao
import com.example.rickmortyapp.data.local.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
