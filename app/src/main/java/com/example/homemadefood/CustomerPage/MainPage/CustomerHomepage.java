package com.example.homemadefood.CustomerPage.MainPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.homemadefood.CustomerPage.BottomSheetDialog.DeliveryFeeListener;
import com.example.homemadefood.CustomerPage.BottomSheetDialog.DeliveryFeeBottomSheetFragment;
import com.example.homemadefood.CustomerPage.BottomSheetDialog.PriceBottomSheetFragment;
import com.example.homemadefood.CustomerPage.DemoAddRestaurants;
import com.example.homemadefood.CustomerPage.Map.MapsActivity;
import com.example.homemadefood.LoginActivity;
import com.example.homemadefood.R;
import com.example.homemadefood.UserProfileActivity;

public class CustomerHomepage extends AppCompatActivity implements DeliveryFeeListener {
    private ImageView searchIcon;
    private SearchView searchView;
    private ResListViewFragment listViewFragment;
    private String state = "All";
    private boolean isRotated = false;
    private ImageButton lastClickedButton;
    private float maxDeliveryFee;
    private static final String PREFS_NAME = "DeliveryPrefs";
    private static final String INTERVAL_KEY = "selectedInterval";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        LinearLayout openingSection = findViewById(R.id.openingSection);
        LinearLayout titleForHorizontalRV = findViewById(R.id.titleForHorizontalRecyclerView);
        searchIcon = findViewById(R.id.search_icon);
        searchView = findViewById(R.id.searchView);
        Button backButton = findViewById(R.id.backButton);

        ImageButton profileButton = findViewById(R.id.profileButton);
        ImageButton cartButton = findViewById(R.id.cardButton);
        ImageButton mapButton = findViewById(R.id.mapButton);
        ImageButton fastFoodButton = findViewById(R.id.fastFoodCategory);
        ImageButton breakfastButton = findViewById(R.id.breakfastCategory);
        ImageButton coffeeButton = findViewById(R.id.coffeeCategory);
        ImageButton pizzaButton = findViewById(R.id.pizzaCategory);
        ImageButton burgerButton = findViewById(R.id.burgerCategory);

        Button deliveryButton = findViewById(R.id.deliveryFee);
        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDistanceBottomSheetDialog();
            }
        });

        Button priceButton = findViewById(R.id.price);
        priceButton.setOnClickListener(view -> showPriceBottomSheetDialog());

        listViewFragment = new ResListViewFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.flContainer, listViewFragment)
                .commit();

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CustomerHomepage.this, profileButton);
                popupMenu.getMenuInflater().inflate(R.menu.profile_pop_up_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.viewAccount) {
                            Intent intent = new Intent(CustomerHomepage.this, UserProfileActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.changePassword) {

                        } else if (menuItem.getItemId() == R.id.logOut) {
                            logout();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHomepage.this, DemoAddRestaurants.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                listViewFragment.searchList(query);
                searchView.setQuery(query, false);
                backButton.setVisibility(View.VISIBLE);

                if (listViewFragment.adapter1.getItemCount() == 0) {
                    openingSection.setVisibility(View.GONE);
                    findViewById(R.id.noResultFound).setVisibility(View.VISIBLE);
                    findViewById(R.id.categorySection).setVisibility(View.GONE);
                    findViewById(R.id.filterSection).setVisibility(View.GONE);
                } else {
                    openingSection.setVisibility(View.GONE);
                    findViewById(R.id.categorySection).setVisibility(View.GONE);
                    findViewById(R.id.noResultFound).setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewFragment.searchList(newText);
                findViewById(R.id.noResultFound).setVisibility(View.GONE);
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    openingSection.setVisibility(View.GONE);
                    titleForHorizontalRV.setVisibility(View.GONE);
                    listViewFragment.nestedScrollView.setVisibility(View.GONE);
                    mapButton.setVisibility(View.GONE);
                    backButton.setVisibility(View.VISIBLE);
                    searchIcon.setVisibility(View.GONE);
                    findViewById(R.id.categorySection).setVisibility(View.GONE);
                    findViewById(R.id.filterSection).setVisibility(View.GONE);
                } else {
                    openingSection.setVisibility(View.VISIBLE);
                    titleForHorizontalRV.setVisibility(View.VISIBLE);
                    listViewFragment.nestedScrollView.setVisibility(View.VISIBLE);
                    backButton.setVisibility(View.GONE);
                    mapButton.setVisibility(View.VISIBLE);
                    findViewById(R.id.categorySection).setVisibility(View.VISIBLE);
                    findViewById(R.id.filterSection).setVisibility(View.VISIBLE);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewFragment.queryRestaurants(maxDeliveryFee, state);
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                backButton.setVisibility(View.GONE);
                searchIcon.setVisibility(View.VISIBLE);
                openingSection.setVisibility(View.VISIBLE);
                findViewById(R.id.categorySection).setVisibility(View.VISIBLE);
                findViewById(R.id.filterSection).setVisibility(View.VISIBLE);
                findViewById(R.id.noResultFound).setVisibility(View.GONE);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHomepage.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        fastFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateButton(fastFoodButton, "Fast Food");
            }
        });

        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateButton(breakfastButton, "Breakfast");
            }
        });

        coffeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateButton(coffeeButton, "Coffee");
            }
        });

        pizzaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateButton(pizzaButton, "Pizza");
            }
        });

        burgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateButton(burgerButton, "Burger");
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedInterval = sharedPreferences.getInt(INTERVAL_KEY, 0);
        if (savedInterval != 0) {
            maxDeliveryFee = getDeliveryFeeForInterval(savedInterval);
        }
    }

    private void rotateButton(ImageButton button, String category) {
        RotateAnimation animation;
        // If the clicked button is different from the last clicked button, reset the last clicked button
        if (lastClickedButton != null && lastClickedButton != button) {
            resetButtonAnimation(lastClickedButton);
            lastClickedButton.clearAnimation();
            isRotated = false;
        }

        if (!isRotated && button.getRotation() == 0) {
            animation = new RotateAnimation(0, -20,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            state = category;
            listViewFragment.queryRestaurants(maxDeliveryFee, state);
        } else {
            animation = new RotateAnimation(-20, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            state = "All";
            listViewFragment.queryRestaurants(maxDeliveryFee, state);
        }
        animation.setDuration(200);
        animation.setFillAfter(true);
        button.startAnimation(animation);

        isRotated = !isRotated; // Toggle between the state
        lastClickedButton = button; // Set the current clicked button as the last clicked button
    }

    private void resetButtonAnimation(ImageButton button) { // Reset animation
        RotateAnimation animation = new RotateAnimation(-20, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(0);
        animation.setFillAfter(true);
        button.startAnimation(animation);

    }

    private void logout() {
        Intent intent = new Intent(CustomerHomepage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDistanceBottomSheetDialog() {
        DeliveryFeeBottomSheetFragment distanceBottomSheet = new DeliveryFeeBottomSheetFragment();
        distanceBottomSheet.setListener(this); // Set the listener to the activity
        distanceBottomSheet.show(getSupportFragmentManager(), null);
    }

    private void showPriceBottomSheetDialog() {
        PriceBottomSheetFragment priceBottomSheet = new PriceBottomSheetFragment();
        priceBottomSheet.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onDeliveryFeeSelected(int selectedInterval) {
        maxDeliveryFee = getDeliveryFeeForInterval(selectedInterval);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(INTERVAL_KEY, selectedInterval);
        editor.apply();
        if (state.equals("All")) {
            listViewFragment.queryRestaurants(maxDeliveryFee, state);
        } else {
            listViewFragment.queryRestaurants(maxDeliveryFee, state);
        }
    }

    @Override
    public void onResetClicked() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(INTERVAL_KEY);
        editor.apply();
        if (state.equals("All")) {
            listViewFragment.restoreOriginalList();
        } else {
            listViewFragment.queryRestaurants(maxDeliveryFee, state);
        }
    }

    private float getDeliveryFeeForInterval(int interval) {
        switch (interval) {
            case 0:
                return 1.0f;
            case 1:
                return 3.0f;
            case 2:
                return 5.0f;
            default:
                return 0.0f;
        }
    }
}
