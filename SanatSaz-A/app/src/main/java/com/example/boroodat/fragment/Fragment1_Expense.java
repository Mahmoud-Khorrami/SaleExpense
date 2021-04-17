package com.example.boroodat.fragment;


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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.activity.Activity15_ExpenseReports;
import com.example.boroodat.activity.Activity16_RecordExpense;
import com.example.boroodat.activity.Activity18_RawMaterial;
import com.example.boroodat.database.Report_DB;
import com.example.boroodat.databinding.F1SalaryAddBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Personnel;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.R;
import com.example.boroodat.databinding.Fragment1Binding;
import com.example.boroodat.general.User_Info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment1_Expense extends Fragment
{

    private Fragment1Binding binding;
    private android.app.AlertDialog progressDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private Realm realm;

    public Fragment1_Expense()
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
        binding = Fragment1Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(getContext(),R.style.Custom);
        progressDialog.setCancelable(false);

        //-------------------------------------------------------------------------------------------------------

        binding.rSalary.addTextChangedListener(new NumberTextWatcherForThousand(binding.rSalary));
        binding.rMaterial.addTextChangedListener(new NumberTextWatcherForThousand(binding.rMaterial));
        binding.rDebtMaterial.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDebtMaterial));
        binding.rOtherExpense.addTextChangedListener(new NumberTextWatcherForThousand(binding.rOtherExpense));
        binding.rDeptOtherExpense.addTextChangedListener(new NumberTextWatcherForThousand(binding.rDeptOtherExpense));
        binding.rSumExpense.addTextChangedListener(new NumberTextWatcherForThousand(binding.rSumExpense));
        binding.rSumDept.addTextChangedListener(new NumberTextWatcherForThousand(binding.rSumDept));

        setData();
        //-------------------------------------------------------------------------------------------------------

        binding.salary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(getContext()).check())
                    salaryDialog();
                else
                    new Internet(getContext()).enable();
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.rawMaterial.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity18_RawMaterial.class);
                intent.putExtra("company_id",new User_Info().company_id());
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("from","manager");
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.otherExpenses.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getContext(), Activity16_RecordExpense.class);
                intent.putExtra("company_id",new User_Info().company_id());
                intent.putExtra("token",new User_Info().token());
                intent.putExtra("from","manager");
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.reports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getData6();
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

    private void salaryDialog()
    {
        final F1SalaryAddBinding binding1 = F1SalaryAddBinding.inflate(LayoutInflater.from(getContext()));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(getContext());
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
                new Personnel(getContext()).dialog(binding1.personnelName,binding1.personnelId);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.accountTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(getContext(),"manager").dialog(binding1.accountTitle,binding1.accountId);
            }
        });
        //----------------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,getContext()).setDate();
            }
        });

        //----------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button add = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                add.setTextColor(getContext().getResources().getColor(R.color.black));
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
                            Toast.makeText(getContext(), "حداقل یک از آیتم های حقوق ، بیعانه ، بیمه و مالیات باید تکمیل شود.", Toast.LENGTH_LONG).show();
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

                            if (new Internet(getContext()).check())
                            {
                                createSalary(binding1.personnelId.getText().toString(),salary1,earnest1,insurance_tax1,binding1.accountId.getText().toString(),binding1.date.getText().toString(),description1,alertDialog);
                            }
                            else
                                new Internet(getContext()).enable();

                        }
                    }
                });


                Button cancel = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                cancel.setTextColor(getContext().getResources().getColor(R.color.black));
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

        alertDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = getContext().getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createSalary(final String personnel_id, final String salary, final String earnest, final String insurance_tax, final String account_id, String date, String description, final AlertDialog alertDialog)
    {
        String url = getContext().getString(R.string.domain) + "api/salary/create";
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
            object.put("secret_key", getContext().getString(R.string.secret_key));
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

                String amount=Double.parseDouble(salary)+Double.parseDouble(earnest)+Double.parseDouble(insurance_tax)+"";
                new Account().decrease(account_id,amount);

                new Report().salary(amount,"i");
                //-------------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(getContext(), "پرداخت با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getData6()
    {
        String url = getString(R.string.domain) + "api/general/data6";
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
                    JSONArray array1 = response.getJSONArray("accounts");
                    JSONArray array2 = response.getJSONArray("personnel");
                    JSONArray array3 = response.getJSONArray("salaries");
                    JSONArray array4 = response.getJSONArray("expenses");
                    JSONArray array5 = response.getJSONArray("materials");

                    boolean b1 = new SaveData(array1).toActivity7DB();
                    boolean b2 = new SaveData(array2).toActivity14DB();
                    boolean b3 = new SaveData(array3).toFragment7DB();
                    boolean b4 = new SaveData(array4).toFragment9DB();
                    boolean b5 = new SaveData(array5).toFragment8DB();

                    if (b1 & b2 & b3 & b4 & b5)
                    {
                        Intent intent=new Intent(getContext(), Activity15_ExpenseReports.class);
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

        String salary=results.get(0).getSalary();
        String material=results.get(0).getMaterial();
        String material_payment=results.get(0).getMaterial_payment();
        String expense=results.get(0).getExpense();
        String expense_payment=results.get(0).getExpense_payment();

        binding.rSalary.setText(salary);
        binding.rMaterial.setText(material);

        double d1=Double.parseDouble(material)-Double.parseDouble(material_payment);
        binding.rDebtMaterial.setText(Math.round(d1)+"");

        binding.rOtherExpense.setText(expense);

        double d2=Double.parseDouble(expense)-Double.parseDouble(expense_payment);
        binding.rDeptOtherExpense.setText(Math.round(d2)+"");

        double d3 = Double.parseDouble(salary) + Double.parseDouble(material) + Double.parseDouble(expense);
        binding.rSumExpense.setText(Math.round(d3)+"");

        double d4 = d1+d2;
        binding.rSumDept.setText(Math.round(d4)+"");



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
