package com.youssef.foodplanner.model.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaResponse {
  private   List<Area> meals;

  public List<Area> getArea() {
    return meals;
  }

  public void setArea(List<Area> meals) {
    meals = meals;
  }
}
