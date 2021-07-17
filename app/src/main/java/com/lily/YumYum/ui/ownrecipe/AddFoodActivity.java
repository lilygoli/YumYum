package com.lily.YumYum.ui.ownrecipe;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lily.YumYum.R;
import com.lily.YumYum.databinding.RecipeFormBinding;
import com.lily.YumYum.model.Food;
import com.lily.YumYum.ui.details.DetailActivity;
import com.lily.YumYum.utils.AppExecutors;
import com.lily.YumYum.utils.JsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class AddFoodActivity extends AppCompatActivity {


    private RecipeFormBinding mBinding;
    private Food food;
    private String name;
    private String cuisine;
    private String description;
    private List<String> diets;
    private List<String> ingredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();
    private List<String> measures = new ArrayList<>();
    private List<String> units = new ArrayList<>();
    private final String image = "https://icon-library.com/images/order-food-icon/order-food-icon-21.jpg";

    private final JsonUtils jutil = new JsonUtils();
    private final AppExecutors mExecutors = AppExecutors.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.recipe_form);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }


        final OwnFoodListViewModel model = OwnRecipeActivity.getmViewModel();

        final int[] prev_id = {findViewById(R.id.x).getId()};

        Button ingredient_add = findViewById(R.id.add_ingredient_button);
        ingredient_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout layout = (RelativeLayout) findViewById(R.id.ingredients);
                LayoutInflater inflater = LayoutInflater.from(AddFoodActivity.this);
                View x = inflater.inflate(R.layout.ingredient, null);
                int id = View.generateViewId();
                x.setId(id);
                Button ingredient_add = findViewById(R.id.add_ingredient_button);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, id);
                ingredient_add.setLayoutParams(params);

                params.addRule(RelativeLayout.BELOW, prev_id[0]);
                x.setLayoutParams(params);
                layout.addView(x);
                prev_id[0] = id;
            }
        });

        final int[] prev_id_step = {findViewById(R.id.step).getId()};
        Button step_add = findViewById(R.id.add_step_button);
        step_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout layout = (RelativeLayout) findViewById(R.id.steps);
                EditText x = new EditText(AddFoodActivity.this);
                int id = View.generateViewId();
                x.setId(id);
                x.setHint("Step");
                Button add_step_button = findViewById(R.id.add_step_button);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, id);
                add_step_button.setLayoutParams(params);

                params.addRule(RelativeLayout.BELOW, prev_id_step[0]);
                x.setLayoutParams(params);
                layout.addView(x);
                prev_id_step[0] = id;
            }
        });

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVars();
                for (String ing :
                        ingredients) {
//                    Log.i("STEEEEPSSSS", ing);
                }
                if (name.isEmpty() || ingredients.isEmpty() || description.isEmpty()) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Cannot Submit")
                            .setMessage("Please fill out name, description and ingredients")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                food = new Food(name, diets, cuisine, description, image, ingredients, steps);

                final String[] foodJson = {""};
                try {
                    foodJson[0] = jutil.foodToJson(food, units, measures);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!foodJson[0].equals("")) {
                            try {
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("own4.txt", Context.MODE_APPEND));
                                outputStreamWriter.write(foodJson[0]);
                                outputStreamWriter.write("#");
                                outputStreamWriter.close();
                            } catch (IOException e) {
                                Timber.e("File write failed: %s", e.toString());
                            }
                        }
                    }
                });
                List<Food> f = model.getfoodList().getValue();
                if(f != null){
                    f.add(food);
                }else{
                    f = (List<Food>) new ArrayList<Food>();
                    f.add(food);
                }
                model.postFoodValue(f);
                onBackPressed();
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void setVars() {
        EditText nameEditText = (EditText) findViewById(R.id.food_name);
        name = nameEditText.getText().toString();

        EditText cuisineEditText = (EditText) findViewById(R.id.cuisine);
        cuisine = cuisineEditText.getText().toString();

        EditText dietEditText = (EditText) findViewById(R.id.diets);
        diets = Arrays.asList(dietEditText.getText().toString().split(","));

        EditText descriptionText = (EditText) findViewById(R.id.description);
        description = descriptionText.getText().toString();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ingredients);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v.getClass() != AppCompatButton.class) {
                for (int k = 0; k < ((ViewGroup) v).getChildCount(); k++) {
                    View v3 = ((ConstraintLayout) v).getChildAt(k);
                    for (int j = 0; j < ((ViewGroup) v3).getChildCount(); j++) {
                        View v2 = ((LinearLayout) v3).getChildAt(j);
                        if (v2.getClass() != Button.class) {
                            switch (j) {
                                case 0:
                                    ingredients.add(((EditText) v2).getText().toString());
                                    break;
                                case 1:
                                    measures.add(((EditText) v2).getText().toString());
                                    break;
                                case 2:
                                    units.add(((EditText) v2).getText().toString());
                                    break;
                            }
                        }

                    }
                }
            }
        }

        RelativeLayout layout_steps = (RelativeLayout) findViewById(R.id.steps);
        for (int i = 0; i < layout_steps.getChildCount(); i++) {
            View v = layout_steps.getChildAt(i);
            if (v.getClass() != Button.class && v.getClass() != AppCompatButton.class) {
                steps.add(((EditText) v).getText().toString());
            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }


}