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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.activity.Activity16_RecordExpense;
import com.example.boroodat.activity.Activity18_RawMaterial;
import com.example.boroodat.databinding.F2SalaryAddBinding;
import com.example.boroodat.databinding.F2DepositAddBinding;
import com.example.boroodat.databinding.Fragment2Binding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Personnel;
import com.example.boroodat.general.User_Info;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Fragment2
{

    private Fragment2Binding binding;
    private android.app.AlertDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private Context context;

    public Fragment2(Context context)
    {
        this.context = context;

        //-------------------------------------------------------

        progressDialog = new SpotsDialog(context,R.style.Custom);
        progressDialog.setCancelable(false);
    }

    public View getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = Fragment2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //-------------------------------------------------------------------------------------------------------

        binding.salary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                    salaryDialog();
                else
                    new Internet(context).enable();
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.rawMaterial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity18_RawMaterial.class);
                intent.putExtra("from","manager");
                context.startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.otherExpenses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(context, Activity16_RecordExpense.class);
                intent.putExtra("from","manager");
                context.startActivity(intent);
            }
        });
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

        return view;
    }

    private void salaryDialog()
    {
        final F2SalaryAddBinding binding1 = F2SalaryAddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.salary.addTextChangedListener(new NumberTextWatcherForThousand(binding1.salary));
        binding1.earnest.addTextChangedListener(new NumberTextWatcherForThousand(binding1.earnest));
        binding1.insuranceTax.addTextChangedListener(new NumberTextWatcherForThousand(binding1.insuranceTax));

        //----------------------------------------------------------------------------------------------------------

        binding1.personnelName.addTextChangedListener(new ClearError(binding1.til1));
        binding1.accountTitle.addTextChangedListener(new ClearError(binding1.til2));
        binding1.date.addTextChangedListener(new ClearError(binding1.til3));

        //----------------------------------------------------------------------------------------------------------

        binding1.personnelName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Personnel(context).show(binding1.personnelName,binding1.personnelId);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.accountTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,"manager").show(binding1.accountTitle,binding1.accountId);
            }
        });
        //----------------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,context).setDate();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(context.getResources().getColor(R.color.black));
                add.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (binding1.personnelName.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til1.getTop());
                            binding1.til1.setError("نام و نام خانوادگی را وارد کنید.");
                        }
                        else if (binding1.salary.getText().toString().equals("") && binding1.earnest.getText().toString().equals("") && binding1.insuranceTax.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til4.getTop());
                            Toast.makeText(context, "حداقل یک از آیتم های حقوق ، بیعانه ، بیمه و مالیات باید تکمیل شود.", Toast.LENGTH_LONG).show();
                        }
                        else if (binding1.accountTitle.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("حساب بانکی را مشخص کنید.");
                        }
                        else if (binding1.date.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til3.getTop());
                            binding1.til3.setError("تاریخ پرداخت را وارد کنید.");
                        }

                        else
                        {
                            String salary1="0";
                            String earnest1="0";
                            String insurance_tax1="0";
                            String description1="-";

                            if (!binding1.salary.getText().toString().equals(""))
                                salary1=NumberTextWatcherForThousand.trimCommaOfString(binding1.salary.getText().toString());

                            if (!binding1.earnest.getText().toString().equals(""))
                                earnest1=NumberTextWatcherForThousand.trimCommaOfString(binding1.earnest.getText().toString());

                            if (!binding1.insuranceTax.getText().toString().equals(""))
                                insurance_tax1=NumberTextWatcherForThousand.trimCommaOfString(binding1.insuranceTax.getText().toString());

                            if (!binding1.description.getText().toString().equals(""))
                                description1=binding1.description.getText().toString();

                            if (new Internet(context).check())
                            {
                                createSalary(binding1.personnelId.getText().toString(),salary1,earnest1,insurance_tax1,binding1.accountId.getText().toString(),binding1.date.getText().toString(),description1,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(context.getResources().getColor(R.color.black));
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

        //------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createSalary(final String personnel_id, final String salary, final String earnest, final String insurance_tax, final String account_id, String date, String description, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/salary/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("personnel_id", personnel_id);
            object.put("salary",salary);
            object.put("earnest",earnest);
            object.put("insurance_tax", insurance_tax);
            object.put("account_id",account_id);
            object.put("date",date);
            object.put("description",description);
            object.put("secret_key", context.getString(R.string.secret_key));
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
                Toast.makeText(context, "پرداخت با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                alertDialogBuilder = null;

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(context, "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void depositDialog()
    {
        final F2DepositAddBinding binding1 = F2DepositAddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
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
                new Account(context,"manager").show(binding1.account,binding1.accountId);
                binding1.account.setError(null);
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,context).setDate();
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
                add.setTextColor(context.getResources().getColor(R.color.black));

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

                            if (new Internet(context).check())
                            {
                                deposit(binding1.title.getText().toString(),amount,binding1.accountId.getText().toString(),binding1.date.getText().toString(), description,alertDialog);
                            }
                            else
                                new Internet(context).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(context.getResources().getColor(R.color.black));

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

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void deposit(String title, final String amount, final String account_id, String date, String description, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/deposit/create";
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
            object.put("secret_key", context.getString(R.string.secret_key));
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
                Toast.makeText(context, "واریز وجه با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                alertDialogBuilder = null;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                Toast.makeText(context, "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
