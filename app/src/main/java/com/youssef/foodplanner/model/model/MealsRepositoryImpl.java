package com.youssef.foodplanner.model.model;

import com.youssef.foodplanner.db.localdata.MealLocalDataSource;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSource;
import com.youssef.foodplanner.db.remotedata.MealRemoteDataSourceImpl;
import com.youssef.foodplanner.db.remotedata.NetworkCallBack;

import java.util.List;

public class MealsRepositoryImpl {

    MealRemoteDataSourceImpl remoteDataSource;
    MealLocalDataSourceImpl localDataSource;
    private static MealsRepositoryImpl repo=null;

    public static MealsRepositoryImpl getInstance(
            MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
        if (repo == null) {
            repo = new MealsRepositoryImpl(remoteDataSource, localDataSource);
        }
        return repo;
    }
    private MealsRepositoryImpl(MealRemoteDataSource remoteDataSource, MealLocalDataSource localDataSource) {
                this.remoteDataSource = remoteDataSource;
                this.localDataSource = localDataSource;
    }
    public void getAllMeals(NetworkCallBack callBack){
        remoteDataSource.makeNetworkCall(callBack);
    }
    public void insertMeals(List<Meal> meals){
        localDataSource.insertMeals(meals);
        }
    public void deleteAllMeals(){
        localDataSource.delete AllMeals();
    }
    public void addtoMealPlan(Meal meal){
    }

    public  static liveData<List<Meal>> getAllMeals(){
        return localDataSource.getAllMeals();
    }
    }


