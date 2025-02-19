package com.youssef.foodplanner.MealPlan.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.youssef.foodplanner.MealPlan.presenter.MealPlanPresenter;
import com.youssef.foodplanner.MealPlan.presenter.MealPlanPresenterImpl;
import com.youssef.foodplanner.R;
import com.youssef.foodplanner.db.localdata.MealLocalDataSourceImpl;
import com.youssef.foodplanner.model.model.Meal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

    public class MealPlanFragment extends Fragment implements MealPlanView , MealPlanAdapter.OnMealClickListener {

    private RecyclerView mealsRecyclerView;
    private CalendarView calendarView;
    private MealPlanAdapter adapter;
    private MealPlanPresenter presenter;
    private String selectedDate;
    private CompositeDisposable disposables;
    private NavController navController;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedDate = getArguments().getString("selectedDate");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposables = new CompositeDisposable();
        navController = Navigation.findNavController(view);
        presenter = new MealPlanPresenterImpl(
                this,
                MealLocalDataSourceImpl.getInstance(requireContext())
        );

        initializeViews(view);
        setupRecyclerView();
        setupCalendar();

        // Load initial data
        String initialDate = getSelectedDate();
        presenter.getMealsForDate(initialDate);
    }

    private void initializeViews(View view) {
        mealsRecyclerView = view.findViewById(R.id.mealsRecyclerView);
        calendarView = view.findViewById(R.id.calendarView);
    }

    private void setupRecyclerView() {
        adapter = new MealPlanAdapter(new ArrayList<>(), this);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealsRecyclerView.setAdapter(adapter);
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.US, "%04d-%02d-%02d",
                    year,
                    month + 1,
                    dayOfMonth
            );
            presenter.getMealsForDate(selectedDate);
        });
    }

    // Add the date handling method here
    private String getSelectedDate() {
        if (getArguments() != null && getArguments().containsKey("selectedDate")) {
            selectedDate = getArguments().getString("selectedDate");
        }

        if (selectedDate == null || selectedDate.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            selectedDate = dateFormatter.format(calendar.getTime());
        }

        return selectedDate;
    }

    // Updated onResume method
    @Override

    public void onResume() {
        super.onResume();
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        String currentDate = getSelectedDate();

        disposables.add(
                Single.fromCallable(() -> currentDate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                date -> {
                                    try {
                                        Date parsedDate = dateFormatter.parse(date);
                                        if (parsedDate != null) {
                                            calendarView.setDate(parsedDate.getTime());
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    presenter.getMealsForDate(date);
                                },
                                throwable -> showError(throwable.getMessage())
                        )
        );
    }

    // Implement clearArguments from MealPlanView interface
    @Override
    public void clearArguments() {
        if (getArguments() != null) {
            getArguments().remove("selectedDate");
        }
    }


    @Override
    public void showMeals(List<Meal> meals) {
        adapter.updateMeals(meals);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMealAddedSuccess() {
        Calendar calendar = Calendar.getInstance();
        String currentDate = dateFormatter.format(calendar.getTime());
        presenter.getMealsForDate(currentDate);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposables != null) {
            disposables.dispose();
            disposables = null;
        }
    }

        @Override
        public void onMealClick(Meal meal) {
            Log.d("MealClick", "Meal clicked: " + meal.getMealName());
            Bundle bundle = new Bundle();
            bundle.putString("mealId", meal.getIdMeal()+"");
            navController.navigate(R.id.action_mealPlanFragment_to_detailedMealFragment, bundle);
        }
    }