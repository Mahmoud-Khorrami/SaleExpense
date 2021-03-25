package com.example.boroodat.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.activity.Activity5_SaleReports;
import com.example.boroodat.adapter.Activity7_Adapter;
import com.example.boroodat.database.Activity7_DB;
import com.example.boroodat.database.Fragment4_DB;
import com.example.boroodat.database.Fragment5_DB;
import com.example.boroodat.database.Report_DB;
import com.example.boroodat.databinding.A6Add1Binding;
import com.example.boroodat.databinding.A7AddBinding;
import com.example.boroodat.databinding.F2DepositAddBinding;
import com.example.boroodat.databinding.Fragment2Binding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.TodayDate;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity7_Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment2_Sale extends Fragment
{

    private Fragment2Binding binding;
    private android.app.AlertDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private Realm realm;

    public Fragment2_Sale()
    {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(getContext(),R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------
        binding.rSale.addTextChangedListener(new NumberTextWatcherForThousand(binding.rSale));
        binding.rRecive.addTextChangedListener(new NumberTextWatcherForThousand(binding.rRecive));
        binding.rDemand.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDemand));
        binding.rDeposit.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDeposit));

        setData();
        //-------------------------------------------------------------------------------------------------------

        binding.deposits.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder==null)
                    depositDialog();
            }
        });


        //-------------------------------------------------------------------------------------------------------

        binding.reports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(getContext()).check())
                    getData2();
                else
                    new Internet(getContext()).enable();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public void depositDialog()
    {
        final F2DepositAddBinding binding1 = F2DepositAddBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(view);

        //---------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //---------------------------------------------------------------------------------------------------

        binding1.account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(getContext(),"manager").dialog(binding1.account,binding1.accountId);
                binding1.account.setError(null);
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,getContext()).setDate();
                binding1.date.setError(null);

            }
        });

        //---------------------------------------------------------------------------------------------------

        binding1.amount.addTextChangedListener(new NumberTextWatcherForThousand(binding1.amount));

        //---------------------------------------------------------------------------------------------------

        binding1.title.addTextChangedListener(new ClearError(binding1.til1));
        binding1.amount.addTextChangedListener(new ClearError(binding1.til2));
        binding1.account.addTextChangedListener(new ClearError(binding1.til3));
        binding1.date.addTextChangedListener(new ClearError(binding1.til4));

        //---------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {

                Button add = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(getResources().getColor(R.color.black));

                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        if (binding1.title.getText().toString().equals(""))
                        {
                            binding1.scrollview.scrollTo(0,binding1.til1.getTop());
                            binding1.til1.setError("عنوان را وارد کنید.");
                        }

                        else if (binding1.amount.getText().toString().equals(""))
                        {
                            binding1.scrollview.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("مبلغ را وارد کنید.");
                        }

                        else if (binding1.account.getText().toString().equals(""))
                        {
                            binding1.scrollview.scrollTo(0, binding1.til3.getTop());
                            binding1.til3.setError("حساب بانکی را مشخص کنید.");
                        }

                        else if (binding1.date.getText().toString().equals(""))
                        {
                            binding1.scrollview.scrollTo(0, binding1.til4.getTop());
                            binding1.til4.setError("تاریخ را وارد کنید.");
                        }

                        else
                        {
                            String amount = NumberTextWatcherForThousand
                                    .trimCommaOfString(binding1.amount.getText().toString());

                            String description="-";
                            if (!binding1.description.getText().toString().equals(""))
                                description=binding1.description.getText().toString();

                            if (new Internet(getContext()).check())
                            {
                                deposit(binding1.title.getText().toString(),amount,binding1.accountId.getText().toString(),binding1.date.getText().toString(), description,alertDialog);
                            }
                            else
                                new Internet(getContext()).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
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

        //---------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = getContext().getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void deposit(String title, final String amount, final String account_id, String date, String description, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/deposit/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("title",title);
            object.put("amount", amount);
            object.put("account_id",account_id);
            object.put("date", date);
            object.put("description", description);
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

                new Account().increase(account_id,amount);
                new Report().deposit(amount,"i");
                //-------------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(getContext(), "واریز وجه با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                alertDialogBuilder = null;
                setData();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(getContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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

    public void getData2()
    {
        String url = getString(R.string.domain) + "api/general/data2";
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
                    JSONArray array2=response.getJSONArray("deposits");
                    JSONArray array3=response.getJSONArray("sales");

                    boolean b1=new SaveData(array1).toActivity7DB();
                    boolean b2=new SaveData(array2).toFragment4DB();
                    boolean b3=new SaveData(array3).toFragment5DB();

                    if (b1 & b2 & b3)
                    {
                        Intent intent=new Intent(getContext(), Activity5_SaleReports.class);
                        intent.putExtra("token",new User_Info().token());
                        intent.putExtra("company_id",new User_Info().company_id());
                        startActivity(intent);
                    }

                    else
                        Toast.makeText(getContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();


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

                Toast.makeText(getContext(), "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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

    public void setData()
    {
        RealmResults<Report_DB> results=realm.where(Report_DB.class).findAll();

        String sale=results.get(0).getSale();
        String sale_payment=results.get(0).getSale_payment();
        String deposit=results.get(0).getDeposit();

        binding.rSale.setText(sale);
        binding.rRecive.setText(sale_payment);

        double d1=Double.parseDouble(sale)-Double.parseDouble(sale_payment);
        binding.rDemand.setText(Math.round(d1)+"");

        binding.rDeposit.setText(deposit);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        realm=Realm.getDefaultInstance();
        setData();
    }
}
