package com.example.boroodat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.example.boroodat.database.UserPass_DB;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.databinding.InternetBinding;
import com.example.boroodat.fragment.Fragment0_Setting;
import com.example.boroodat.fragment.Fragment1_Expense;
import com.example.boroodat.fragment.Fragment2_Sale;
import com.example.boroodat.fragment.Fragment3_Home;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.FragmentUtil;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Activity2ManagerBinding;
import com.example.boroodat.general.NavigationIconClickListener;
import com.example.boroodat.general.RuntimePermissionsActivity;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.general.WriteToExcel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity2_Manager extends RuntimePermissionsActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    private Activity2ManagerBinding binding;
    private android.app.AlertDialog progressDialog;
    private Fragment0_Setting fragment0Setting;
    private Fragment1_Expense fragment1Expense;
    private Fragment2_Sale fragment2Sale;
    private Fragment3_Home fragment3Home;
    private boolean doubleBackToExitPressedOnce=false;
    private Context context=this;
    private int code = 10;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity2ManagerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm = Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        binding.appBar.setTitle("");
        binding.title.setText(new User_Info().company_name());
        setSupportActionBar(binding.appBar);

        binding.appBar.setNavigationOnClickListener(new NavigationIconClickListener(context, view.findViewById(R.id.pager_1),
                new AccelerateDecelerateInterpolator(),
                context.getResources().getDrawable(R.drawable.navigation_menu),
                context.getResources().getDrawable(R.drawable.close)));

        //-------------------------------------------------------------------------------------------------------

        View view1 = binding.lnr1;
        MaterialCardView xls=view1.findViewById(R.id.xls);
        MaterialCardView about=view1.findViewById(R.id.about);
        MaterialCardView deleteUserPass=view1.findViewById(R.id.deleteUserPass);

        xls.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (Environment.getExternalStorageDirectory().canWrite())
                    getData14();

                else
                    Activity2_Manager.super.requestAppPermissions ( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code );
            }
        });

        //------------------------------------------------------------

        about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(Activity2_Manager.this,Activity24_AboutUs.class));
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
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view1);

                    //-------------------------------------------------------------------------------------------------------

                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("تایید", null);
                    alertDialogBuilder.setNeutralButton("لغو", null);
                    final AlertDialog alertDialog = alertDialogBuilder.create();

                    //-------------------------------------------------------------------------------------------------------

                    binding1.message.setText("آیا مایلید نام کاربری و رمز عبور را از حافظه حذف کنید؟");

                    //-------------------------------------------------------------------------------------------------------

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface dialogInterface)
                        {
                            Button approve = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
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

                            Button Cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
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

                    //....................................................................................................

                   // alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
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
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        fragment0Setting = new Fragment0_Setting();
        fragment1Expense = new Fragment1_Expense();
        fragment2Sale = new Fragment2_Sale();
        fragment3Home = new Fragment3_Home();

        //----------------------------------------------------------------------------------------------------------

        setupViewPager(binding.pager1);
        binding.pager1.setCurrentItem(1);
        binding.pager1.setPagingEnabled(false);

        //----------------------------------------------------------------------------------------------------------

        binding.bnv1.setOnNavigationItemSelectedListener(this);
        binding.bnv1.setSelectedItemId(R.id.menu_1_4);

        //----------------------------------------------------------------------------------------------------------

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity2_Manager.this, Activity6_RecordSales.class);
                intent.putExtra("company_id",new User_Info().company_id());
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("from","manager");
                startActivity(intent);
            }
        });

        //----------------------------------------------------------------------------------------------------------

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.menu_1_1)
        {
            binding.pager1.setCurrentItem(0);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_2)
        {
            binding.pager1.setCurrentItem(1);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_3)
        {
            binding.pager1.setCurrentItem(2);
            return true;
        }

        if (item.getItemId() == R.id.menu_1_4)
        {
            binding.pager1.setCurrentItem(3);
            return true;
        }

        return false;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        FragmentUtil.ViewPagerAdapter adapter = new FragmentUtil.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(fragment0Setting);
        adapter.addFragment(fragment1Expense);
        adapter.addFragment(fragment2Sale);
        adapter.addFragment(fragment3Home);
        viewPager.setAdapter(adapter);
    }

    public void getData14()
    {
        String url = getString(R.string.domain) + "api/general/data14";
        progressDialog.show();

        JSONObject object = new JSONObject();
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
                new WriteToExcel(context).export(response);
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == code)
            Toast.makeText ( getApplicationContext (),
                    "مجوز دسترسی به حافظه داده شد.", Toast.LENGTH_SHORT ).show ();
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        Toast.makeText ( getApplicationContext (),
                "مجوز دسترسی به حافظه داده نشد.", Toast.LENGTH_LONG ).show ();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج مجددا دکمه بازگشت را فشار دهید.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
