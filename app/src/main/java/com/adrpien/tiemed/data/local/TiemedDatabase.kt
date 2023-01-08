package com.adrpien.tiemed.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adrpien.tiemed.data.local.entities.*
import com.adrpien.tiemed.data.local.relations.RepairPartCrossReference


@Database (
    entities = [
        DeviceEntity::class,
        EstStateEntity::class,
        HospitalEntity::class,
        InspectionStateEntity::class,
        InspectionEntity::class,
        PartEntity::class,
        RepairEntity::class,
        RepairStateEntity::class,
        TechnicianEntity::class,
        RepairPartCrossReference::class

            ],
    version = 1
)
abstract class TiemedDatabase() : RoomDatabase() {


    abstract val tiemedDao: TiemedDao

    companion object {

        @Volatile
        private var tiemedDatabse: TiemedDatabase? = null

        fun getInstance(context: Context): TiemedDatabase {
            return tiemedDatabse ?: Room.databaseBuilder(
                context.applicationContext,
                TiemedDatabase::class.java,
                "tiemed_database"
            ).build().also {
                tiemedDatabse = it
            }
        }
    }

}