package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import com.example.boroodat.database.UserPass_DB;
import com.example.boroodat.databinding.Activity20UserBinding;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NavigationIconClickListener;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity20_User extends AppCompatActivity
{

    private Activity20UserBinding binding;
    private AlertDialog progressDialog;
    private Context context=this;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity20UserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        binding.appBar.setTitle("");
        binding.title.setText(new User_Info().company_name());
        setSupportActionBar(binding.appBar);

        binding.appBar.setNavigationOnClickListener(new NavigationIconClickListener(context, view.findViewById(R.id.scrollView10),
                new AccelerateDecelerateInterpolator(),
                context.getResources().getDrawable(R.drawable.navigation_menu),
                context.getResources().getDrawable(R.drawable.close)));

        //-------------------------------------------------------------------------------------------------------

        View view1 = binding.lnr1;
        MaterialCardView about=view1.findViewById(R.id.about);
        MaterialCardView deleteUserPass=view1.findViewById(R.id.deleteUserPass);
        MaterialCardView xls=view1.findViewById(R.id.xls);

        xls.setVisibility(View.GONE);

        //------------------------------------------------------------

        about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity20_User.this,Activity24_AboutUs.class));
            }
        });


        //------------------------------------------------------------

        deleteUserPass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final RealmResults<UserPass_DB> res = realm.where(UserPass_DB.class)
                        .findAll();
                if (res.size()>0)
                {
                    final Dialog1Binding binding1 = Dialog1Binding.inflate(LayoutInflater.from(context));
                    View view1 = binding1.getRoot();
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view1);

                    //-------------------------------------------------------------------------------------------------------

                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("تایید", null);
                    alertDialogBuilder.setNeutralButton("لغو", null);
                    final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    //-------------------------------------------------------------------------------------------------------

                    binding1.message.setText("آیا مایلید نام کاربری و رمز عبور را از حافظه حذف کنید؟");

                    //-------------------------------------------------------------------------------------------------------

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface dialogInterface)
                        {
                            Button approve = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                            approve.setTextColor(context.getResources().getColor(R.color.black));
                            approve.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {
                                    realm.executeTransaction(new Realm.Transaction()
                                    {
                                        @Override
                                        public void execute(Realm realm)
                                        {
                                            res.deleteAllFromRealm();
                                            Toast.makeText(getApplicationContext(),"حذف نام کاربری رمز عبور از حافظه دستگاه اندرویدی شما با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                            });

                            Button Cancel = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
                            Cancel.setTextColor(context.getResources().getColor(R.color.black));
                            Cancel.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    });

                    //---------------------------------------------------------------------------------------------

                    alertDialog.show();
                    DisplayMetrics display = context.getResources().getDisplayMetrics();
                    int width = display.widthPixels;
                    width = (int) ((width) * ((double) 4 / 5));
                    alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                }

                else
                    Toast.makeText(getApplicationContext(),"نام کاربری و رمز عبور ذخیره نشده است.",Toast.LENGTH_LONG).show();

            }
        });
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------

        binding.sale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity6_RecordSales.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.expense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity16_RecordExpense.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.report.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                    getData8();
                else
                    new Internet(context).enable();
            }
        });

    }

    public void getData8()
    {
        String url = getString(R.string.domain) + "api/general/data8";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
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
                progressDialog.dismiss();

                try
                {
                    JSONArray array1=response.getJSONArray("accounts");
                    JSONArray array2=response.getJSONArray("expenses");
                    JSONArray array3=response.getJSONArray("sales");
                    JSONArray array4=response.getJSONArray("details");

                    boolean b1=new SaveData(array1).toActivity7DB();
                    boolean b2=new SaveData(array2).toFragment9DB();
                    boolean b3=new SaveData(array3).toFragment5DB();
                    boolean b4=new SaveData(array4).toSaleDetailDB();


                    if (b1 & b2 & b3 & b4)
                        startActivity(new Intent(getApplicationContext(), Activity21_UserReport.class));

                    else
                        Toast.makeText(getApplicationContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();


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
}
