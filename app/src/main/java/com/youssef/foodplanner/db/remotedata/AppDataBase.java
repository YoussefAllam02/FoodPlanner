package com.youssef.foodplanner.db.remotedata;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.youssef.foodplanner.db.localdata.MealDao;
import com.youssef.foodplanner.model.model.Meal;

@Database(entities = {Meal.class}, version = 2, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract MealDao mealDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE meals ADD COLUMN date TEXT");
        }
    };



    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "meal_database")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return instance;
    }
}
