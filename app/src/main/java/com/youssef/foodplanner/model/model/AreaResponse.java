package com.youssef.foodplanner.model.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaResponse {
  private   List<Area> Area;

  public List<Area> getArea() {
    return Area;
  }

  public void setArea(List<Area> area) {
    Area = area;
  }
}
