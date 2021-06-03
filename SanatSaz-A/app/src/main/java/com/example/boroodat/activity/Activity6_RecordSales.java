package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity6_Adapter;
import com.example.boroodat.database.Activity6_DB;
import com.example.boroodat.databinding.Activity6SalesBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Buyer;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Driver;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity6_Model;
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

public class Activity6_RecordSales extends AppCompatActivity
{

    private Activity6SalesBinding binding;
    private android.app.AlertDialog progressDialog;
    private List<Activity6_Model> models =new ArrayList<>(  );
    private Activity6_Adapter adapter;
    private Realm realm;
    private Context context=this;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity6SalesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm=Realm.getDefaultInstance();
        //------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(false);

        //------------------------------------------------------------------

        final Bundle extras=getIntent().getExtras();
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

        binding.buyer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Buyer(context,from,binding.buyer,binding.buyerId).show();
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding.driver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Driver(context,from,binding.driver,binding.driverId).show();
            }
        });

        //---------------------------------------------------------------------------------------------------

        clearActivity6DB();
        writeToActivity6DB();

        //---------------------------------------------------------------------------------------------------

        adapter = new Activity6_Adapter(models, getApplicationContext(),binding.sum );
        binding.recyclerView.setLayoutManager ( new LinearLayoutManager( getApplicationContext()) );
        binding.recyclerView.setAdapter (adapter);
        setAdapter();

        //---------------------------------------------------------------------------------------------------

        binding.add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                writeToActivity6DB();
                setAdapter();
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding.remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final RealmResults<Activity6_DB> results = realm.where ( Activity6_DB.class ).findAll();

                if (results.size() > 1)
                {
                    realm.executeTransaction(new Realm.Transaction()
                    {
                        @Override
                        public void execute(Realm realm)
                        {
                            RealmResults<Activity6_DB> res = realm.where(Activity6_DB.class)
                                    .equalTo("id", results.size() - 1)
                                    .findAll();
                            if (res.size() > 0)
                                res.deleteAllFromRealm();
                            setAdapter();
                        }
                    });
                }

                else if (results.size()==1)
                {
                    clearActivity6DB();
                    writeToActivity6DB();
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

        binding.factorNumber.addTextChangedListener(new ClearError(binding.factorNumberTil));
        binding.date.addTextChangedListener(new ClearError(binding.dateTil));
        binding.buyer.addTextChangedListener(new ClearError(binding.buyerTil));
        binding.driver.addTextChangedListener(new ClearError(binding.driverTil));
        binding.accountNumber.addTextChangedListener(new ClearError(binding.accountNumberTil));
        //---------------------------------------------------------------------------------------------------

        binding.accountNumber.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,from).show(binding.accountNumber,binding.txtAccountId);
            }
        });


        //---------------------------------------------------------------------------------------------------

        binding.approve.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final RealmResults<Activity6_DB> results = realm.where ( Activity6_DB.class ).findAll();

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
                        binding.scrollView.scrollTo(0, binding.crdv1.getTop());
                        binding.factorNumberTil.setError("شماره قبض را وارد کنید.");
                    }
                    else if (binding.date.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.crdv1.getTop());
                        binding.dateTil.setError("تاریخ را وارد کنید.");
                    }
                    else if (binding.buyer.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.crdv1.getTop());
                        binding.buyerTil.setError("نام خریدار را وارد کنید.");
                    }
                    else if (binding.driver.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.crdv1.getTop());
                        binding.driverTil.setError("نام راننده را وارد کنید.");
                    }

                    else if (binding.accountNumber.getText().toString().equals(""))
                    {
                        binding.scrollView.scrollTo(0, binding.crdv3.getTop());
                        binding.accountNumberTil.setError("حساب بانکی را مشخص کنید.");
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
                            sale(binding.factorNumber.getText().toString(),binding.date.getText().toString(),binding.buyerId.getText().toString(),binding.driverId.getText().toString(),sum1,payment1,remain1,binding.txtAccountId.getText().toString(),description);
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

    private void clearActivity6DB()
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity6_DB> res = realm.where(Activity6_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });
    }

    private void writeToActivity6DB()
    {
        RealmResults<Activity6_DB> results = realm.where ( Activity6_DB.class ).findAll();

        realm.beginTransaction ();
        realm.copyToRealmOrUpdate ( new Activity6_DB( results.size(),results.size()+1+"","","","","") );
        realm.commitTransaction ();

    }

    private void setAdapter()
    {
        models.clear();

        RealmResults<Activity6_DB> results = realm.where ( Activity6_DB.class ).findAll ();

        for (int i=results.size()-1;i>=0;i--)
        {
            models.add(new Activity6_Model(results.get(i).getId(),results.get(i).getRow(),results.get(i).getDescription(),results.get(i).getNumber(),results.get(i).getUnitPrice(),results.get(i).getTotalPrice()));
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

    public void sale(String factor_number, String date, final String buyer_id, final String driver_id, final String sum, final String payment, final String remain, final String account_id, String description)
    {
        String url = getString(R.string.domain) + "api/sale/create";
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
            object.put("factor_number", factor_number);
            object.put("date", date);
            object.put("buyer_id", buyer_id);
            object.put("driver_id", driver_id);
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
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"ثبت فروش با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

}
