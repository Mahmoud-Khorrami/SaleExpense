package com.example.boroodat.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity16_Adapter;
import com.example.boroodat.adapter.Activity8_Adapter;
import com.example.boroodat.adapter.Activity9_Adapter;
import com.example.boroodat.database.Activity16_DB;
import com.example.boroodat.database.Activity6_DB;
import com.example.boroodat.database.Activity8_DB;
import com.example.boroodat.database.Activity9_DB;
import com.example.boroodat.database.Fragment5_DB;
import com.example.boroodat.database.Fragment9_DB;
import com.example.boroodat.databinding.A6Add1Binding;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.A9AddBinding;
import com.example.boroodat.databinding.Activity16RecordExpenseBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity16_Model;
import com.example.boroodat.model.Activity6_Model;
import com.example.boroodat.model.Activity8_Model;
import com.example.boroodat.model.Activity9_Model;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

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

public class Activity17_EditExpense extends AppCompatActivity
{

    private Activity16RecordExpenseBinding binding;
    private android.app.AlertDialog progressDialog;
    private List<Activity16_Model> models =new ArrayList<>(  );
    private Activity16_Adapter adapter;
    private Realm realm;
    private Context context=this;
    private String last_account_id,last_sum, last_payment, expense_id;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity16RecordExpenseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(false);

        //------------------------------------------------------------------


        final Bundle extras=getIntent().getExtras();
        expense_id =extras.getString("expense_id");
        String factor_number=extras.getString("factor_number");
        String date=extras.getString("date");
        last_sum=extras.getString("sum");
        last_payment=extras.getString("payment");
        last_account_id=extras.getString("account_id");
        String account_title=extras.getString("account_title");
        String description=extras.getString("description");
        from=extras.getString("from");

        //---------------------------------------------------------------------------------------------------

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new Date(binding.date,context).setDate();
            }
        });

        //---------------------------------------------------------------------------------------------------

        adapter = new Activity16_Adapter(models, getApplicationContext(),binding.sum );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getApplicationContext()) );
        binding.recyclerView.setAdapter (adapter);
        setAdapter();

        //---------------------------------------------------------------------------------------------------

        binding.add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                writeToActivity16DB();
                setAdapter();
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding.remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final RealmResults<Activity16_DB> results = realm.where ( Activity16_DB.class ).findAll();

                if (results.size() > 1)
                {
                    realm.executeTransaction(new Realm.Transaction()
                    {
                        @Override
                        public void execute(Realm realm)
                        {
                            RealmResults<Activity16_DB> res = realm.where(Activity16_DB.class)
                                    .equalTo("id", results.get(results.size() - 1).getId())
                                    .findAll();
                            if (res.size() > 0)
                                res.deleteAllFromRealm();
                            setAdapter();
                        }
                    });
                }

                else if (results.size()==1)
                {
                    clearActivity16DB();
                    writeToActivity16DB();
                    setAdapter();
                }
                double sum1=0;
                for (int i=0;i<results.size();i++)
                {
                    if (!results.get(i).getTotalPrice().equals(""))
                        sum1=sum1+Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(results.get(i).getTotalPrice()));
                }

                binding.sum.setText(Math.round(sum1)+"");
            }
        });
        //---------------------------------------------------------------------------------------------------

        binding.sum.addTextChangedListener(new NumberTextWatcherForThousand(binding.sum));
        binding.payment.addTextChangedListener(new NumberTextWatcherForThousand(binding.payment));
        binding.remain.addTextChangedListener(new NumberTextWatcherForThousand(binding.remain));

        //---------------------------------------------------------------------------------------------------

        binding.sum.addTextChangedListener(new Watcher(binding.sum,binding.payment,binding.remain));
        binding.payment.addTextChangedListener(new Watcher(binding.sum,binding.payment,binding.remain));

        //---------------------------------------------------------------------------------------------------

        binding.factorNumber.setText(factor_number);
        binding.date.setText(date);
        binding.sum.setText(last_sum);
        binding.payment.setText(last_payment);
        binding.accountNumber.setText(account_title);
        binding.txtAccountId.setText(last_account_id);
        binding.description.setText(description);

        //---------------------------------------------------------------------------------------------------

        binding.accountNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,from).dialog(binding.accountNumber,binding.txtAccountId);
            }
        });


        //---------------------------------------------------------------------------------------------------

        binding.approve.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final RealmResults<Activity16_DB> results = realm.where ( Activity16_DB.class ).findAll();

                int status=0;
                for (int i=0;i<results.size();i++)
                {
                    if (results.get(i).getDescription().equals("") && results.get(i).getTotalPrice().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "شرح و قیمت کالا در ردیف " + (i + 1) + " را وارد کنید.", Toast.LENGTH_LONG).show();
                        status=1;
                        break;
                    }

                    else if (!results.get(i).getDescription().equals("") && results.get(i).getTotalPrice().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "قیمت کالا در ردیف " + (i + 1) + " را وارد کنید.", Toast.LENGTH_LONG).show();
                        status=1;
                        break;
                    }
                    else if (results.get(i).getDescription().equals("") && !results.get(i).getTotalPrice().equals(""))
                    {
                        Toast.makeText(getApplicationContext(), "شرح کالا در ردیف " + (i + 1) + " را وارد کنید.", Toast.LENGTH_LONG).show();
                        status=1;
                        break;
                    }
                }

                if (status==0)
                {
                    if (binding.factorNumber.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.lnr1.getTop());
                        binding.factorNumber.setError("شماره قبض را وارد کنید.");
                    }

                    else if (binding.date.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.lnr1.getTop());
                        binding.date.setError("تاریخ را وارد کنید.");
                    }

                    else if (binding.accountNumber.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.lnr8.getTop());
                        binding.accountNumber.setError("حساب بانکی را مشخص کنید.");
                    }

                    else
                    {
                        String payment1="0";
                        String remain1="0";
                        String description="-";

                        String sum1 = NumberTextWatcherForThousand
                                .trimCommaOfString(binding.sum.getText().toString());

                        if (!binding.payment.getText().toString().equals(""))
                            payment1 = NumberTextWatcherForThousand
                                    .trimCommaOfString(binding.payment.getText().toString());

                        if (!binding.remain.getText().toString().equals(""))
                            remain1 = NumberTextWatcherForThousand
                                    .trimCommaOfString(binding.remain.getText().toString());

                        if (!binding.description.getText().toString().equals(""))
                            description=binding.description.getText().toString();

                        if (new Internet(getApplicationContext()).check())
                        {
                            editExpense(binding.factorNumber.getText().toString(),binding.date.getText().toString(),sum1,payment1,remain1,binding.txtAccountId.getText().toString(),description);
                        }
                        else
                            new Internet(getApplicationContext()).enable();

                    }
                }
            }
        });

        binding.cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

    }

    private void clearActivity16DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity16_DB> res = realm.where(Activity16_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });
    }

    private void writeToActivity16DB()
    {
        RealmResults<Activity16_DB> results = realm.where ( Activity16_DB.class ).findAll();

        realm.beginTransaction ();
        realm.copyToRealmOrUpdate ( new Activity16_DB( results.size(),results.size()+1+"","","","","") );
        realm.commitTransaction ();

    }

    private void setAdapter()
    {
        models.clear();

        RealmResults<Activity16_DB> results = realm.where ( Activity16_DB.class ).findAll ();

        for (int i=results.size()-1;i>=0;i--)
        {
            models.add(new Activity16_Model(results.get(i).getId(),results.get(i).getRow(),results.get(i).getDescription(),results.get(i).getNumber(),results.get(i).getUnitPrice(),results.get(i).getTotalPrice()));
        }

        adapter.notifyDataSetChanged();

    }

    public class Watcher implements TextWatcher
    {

        private TextInputEditText sum,payment,remain;

        public Watcher(TextInputEditText sum, TextInputEditText payment, TextInputEditText remain)
        {
            this.sum = sum;
            this.payment = payment;
            this.remain = remain;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            double payment1=0;
            double sum1=0;

            if (!sum.getText().toString().equals(""))
                sum1=Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(sum.getText().toString()));
            if (!payment.getText().toString().equals(""))
                payment1=Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(payment.getText().toString()));

            double remain1=sum1-payment1;

            remain.setText(Math.round(remain1)+"");
        }
    }

    public void editExpense(final String factor_number, final String date, final String sum, final String payment, final String remain, final String account_id, String description)
    {
        String url = getString(R.string.domain) + "api/expense/edit";
        progressDialog.show();

        setAdapter();

        Gson gson=new Gson();
        JSONArray details=new JSONArray();
        for (int i=0;i<models.size();i++)
        {
            details.put(gson.toJson(models.get(i)));
        }

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id",new User_Info().company_id());
            object.put("expense_id", expense_id);
            object.put("factor_number", factor_number);
            object.put("date", date);
            object.put("sum", sum);
            object.put("payment", payment);
            object.put("remain", remain);
            object.put("account_id", account_id);
            object.put("description", description);
            object.put("details", details);
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

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment9_DB(Integer.parseInt(expense_id), factor_number, date,sum,payment,account_id ));
                realm.commitTransaction();

                //-------------------------------------------------------------------------------

                new Account().increase(last_account_id,last_payment);
                new Account().decrease(account_id,payment);

                new Report().expense(last_sum,last_payment,"d");
                new Report().expense(sum,payment,"i");
                //-------------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"ویرایش با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                finish();
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
