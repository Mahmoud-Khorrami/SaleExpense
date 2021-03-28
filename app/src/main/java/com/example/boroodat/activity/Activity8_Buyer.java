package com.example.boroodat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity8_Adapter;
import com.example.boroodat.database.Activity8_DB;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.Activity8BuyerBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.TodayDate;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity8_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity8_Buyer extends AppCompatActivity
{
    Activity8BuyerBinding binding;
    private List<Activity8_Model> models =new ArrayList<>(  );
    private Activity8_Adapter adapter;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity8BuyerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter = new Activity8_Adapter(models, Activity8_Buyer.this,1,"manager");
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( Activity8_Buyer.this ) );
        binding.recyclerView.setAdapter (adapter);
        addBuyer();

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                {
                    if (alertDialogBuilder == null)
                        dialog();
                }
                else
                    new Internet(context).enable();
            }
        });

        //----------------------------------------------------------------------------------------------------------

    }

    private void dialog()
    {
        final A8AddBinding binding1 = A8AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (binding1.name.getText().toString().equals(""))
                            binding1.til1.setError("نام خریدار را وارد کنید.");

                        else
                        {

                            String phone_number1="-";
                            String destination1="-";

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1= binding1.phoneNumber.getText().toString();

                            if (!binding1.destination.getText().toString().equals(""))
                                destination1=binding1.destination.getText().toString();

                            if (new Internet(context).check())
                            {
                                create(binding1.name.getText().toString(),phone_number1,destination1,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(getResources().getColor(R.color.black));
                cancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }
                });
            }
        });

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void create(final String name, final String phone_number, final String destination, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/buyer/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("destination", destination);
            object.put("secret_key", getString(R.string.secret_key));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    int id = Integer.parseInt(response.getString("id"));
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(new Activity8_DB(id, name, phone_number, destination));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ایجاد خریدار جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialogBuilder = null;

                    addBuyer();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        };


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, listener, errorListener)
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer "+ new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void addBuyer()
    {
        RealmResults<Activity8_DB> res = realm.where(Activity8_DB.class).findAll();

        models.clear();

        for (int i=0;i<res.size();i++)
        {
            models.add(new Activity8_Model(res.get(i).getId(),res.get(i).getName(),res.get(i).getPhone_number(),res.get(i).getDestination()));
        }

        adapter.notifyDataSetChanged();
    }
}
