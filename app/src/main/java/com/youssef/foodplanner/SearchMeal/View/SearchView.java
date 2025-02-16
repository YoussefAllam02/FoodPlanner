    package com.youssef.foodplanner.SearchMeal.View;

    import com.youssef.foodplanner.model.model.Area;
    import com.youssef.foodplanner.model.model.Category;
    import com.youssef.foodplanner.model.model.Ingredient;
    import com.youssef.foodplanner.model.model.Meal;

    import java.util.List;

    public interface SearchView {
       // void showMeals(List<Meal> meals);
        void showAreas(List<Area> areas);
        void showCategories(List<Category> categories);
        void showIngredients(List<Ingredient> ingredients);
        void showError(String message);
    }