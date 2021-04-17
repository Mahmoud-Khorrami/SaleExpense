package com.example.boroodat.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.adapter.Activity6_Adapter;
import com.example.boroodat.adapter.Activity8_Adapter;
import com.example.boroodat.adapter.Activity9_Adapter;
import com.example.boroodat.database.Activity6_DB;
import com.example.boroodat.database.Activity7_DB;
import com.example.boroodat.database.Activity8_DB;
import com.example.boroodat.database.Activity9_DB;
import com.example.boroodat.databinding.A6Add1Binding;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.A9AddBinding;
import com.example.boroodat.databinding.Activity6SalesBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.User_Info;
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

public class Activity6_RecordSales extends AppCompatActivity
{

    private Activity6SalesBinding binding;
    private android.app.AlertDialog progressDialog;
    private List<Activity6_Model> models =new ArrayList<>(  );
    private List<Activity8_Model> models2 =new ArrayList<>(  );
    private List<Activity9_Model> models3 =new ArrayList<>(  );
    private Activity6_Adapter adapter;
    private Activity8_Adapter adapter2;
    private Activity9_Adapter adapter3;
    private Realm realm;
    private Context context=this;
    private AlertDialog.Builder alertDialogBuilder=null;
    private AlertDialog.Builder alertDialogBuilder1=null;
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
                buyerDialog(binding.buyer,binding.buyerId);
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding.driver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                driverDialog(binding.driver,binding.driverId);
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
                new Account(context,from).dialog(binding.accountNumber,binding.txtAccountId);
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

                new Account().increase(account_id,payment);
                new Report().sale(sum,payment,"i");

                //-------------------------------------------------------------------------------

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void buyerDialog(TextView buyerName,TextView buyerId)
    {
        final A6Add1Binding binding1 = A6Add1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ایجاد خریدار جدید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        binding1.title.setText("لیست خریدارها");

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter2 = new Activity8_Adapter(models2, context,2,buyerName,buyerId,alertDialog,from );
        binding1.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        binding1.recyclerView.setAdapter (adapter2);
        addBuyer();

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");
        searchItem.add("مقصد");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding1.spinner.setAdapter(adp);
        binding1.spinner.setSelection(0);

        //-------------------------------------------------------------------------------------------------------

        binding1.lnr2.setVisibility(View.GONE);

        binding1.search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding1.lnr1.setVisibility(View.GONE);
                binding1.lnr2.setVisibility(View.VISIBLE);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                List<Activity8_Model> newList = new ArrayList<>();

                for (int i=0;i<models2.size();i++)
                {
                    if (binding1.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی")
                            && models2.get(i).getName().toLowerCase().contains(newText))
                        newList.add(models2.get(i));

                    else if (binding1.spinner.getSelectedItem().toString().equals("شماره همراه")
                            && models2.get(i).getPhone_number().toLowerCase().contains(newText))
                        newList.add(models2.get(i));

                    else if (binding1.spinner.getSelectedItem().toString().equals("مقصد")
                            && models2.get(i).getDestination().toLowerCase().contains(newText))
                        newList.add(models2.get(i));
                }

                adapter2.setFilter(newList);
                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                adapter2.setFilter(models2);
                binding1.lnr1.setVisibility(View.VISIBLE);
                binding1.lnr2.setVisibility(View.GONE);
                return true;
            }
        });

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
                        if (new Internet(context).check())
                        {
                            if (alertDialogBuilder1 == null)
                                addBuyerDialog();
                        }
                        else
                            new Internet(context).enable();
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

        //----------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addBuyerDialog()
    {
        final A8AddBinding binding1 = A8AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder1 = new AlertDialog.Builder(context);
        alertDialogBuilder1.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setPositiveButton("تایید", null);
        alertDialogBuilder1.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder1.create();

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
                                createBuyer(binding1.name.getText().toString(),phone_number1,destination1,alertDialog);
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
                        alertDialogBuilder1 = null;
                    }
                });
            }
        });

        //------------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void createBuyer(final String name, final String phone_number, final String destination, final AlertDialog alertDialog)
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
                    realm.copyToRealmOrUpdate(new Activity8_DB(id, name, phone_number, destination,""));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ایجاد خریدار جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialogBuilder1 = null;

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

        models2.clear();

        for (int i=0;i<res.size();i++)
        {
            if (!res.get(i).getArchive().equals("done"))
                models2.add(new Activity8_Model(res.get(i).getId(), res.get(i).getName(), res.get(i).getPhone_number(), res.get(i).getDestination(), res.get(i).getArchive()));
        }

        adapter2.notifyDataSetChanged();
    }

    private void driverDialog(TextView driverName,TextView driverId)
    {
        final A6Add1Binding binding1 = A6Add1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ایجاد راننده جدید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        binding1.title.setText("لیست راننده ها");

        //----------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        adapter3 = new Activity9_Adapter(models3, context,2,driverName,driverId,alertDialog,from );
        binding1.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        binding1.recyclerView.setAdapter (adapter3);
        addDriver();

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> searchItem=new ArrayList<>();
        searchItem.add("نام و نام خانوادگی");
        searchItem.add("شماره همراه");
        searchItem.add("مشخصات خودرو");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, searchItem);
        binding1.spinner.setAdapter(adp);
        binding1.spinner.setSelection(0);

        //-------------------------------------------------------------------------------------------------------

        binding1.lnr2.setVisibility(View.GONE);

        binding1.search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                binding1.lnr1.setVisibility(View.GONE);
                binding1.lnr2.setVisibility(View.VISIBLE);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                newText = newText.toLowerCase();
                List<Activity9_Model> newList = new ArrayList<>();

                for (int i=0;i<models3.size();i++)
                {
                    if (binding1.spinner.getSelectedItem().toString().equals("نام و نام خانوادگی")
                            && models3.get(i).getName().toLowerCase().contains(newText))
                        newList.add(models3.get(i));

                    else if (binding1.spinner.getSelectedItem().toString().equals("شماره همراه")
                            && models3.get(i).getPhone_number().toLowerCase().contains(newText))
                        newList.add(models3.get(i));

                    else if (binding1.spinner.getSelectedItem().toString().equals("مشخصات خودرو")
                            && models3.get(i).getCar_type().toLowerCase().contains(newText))
                        newList.add(models3.get(i));
                }

                adapter3.setFilter(newList);
                return true;
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                adapter3.setFilter(models3);
                binding1.lnr1.setVisibility(View.VISIBLE);
                binding1.lnr2.setVisibility(View.GONE);
                return true;
            }
        });

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
                        if (new Internet(context).check())
                        {
                            if (alertDialogBuilder1 == null)
                                addDriverDialog();
                        }
                        else
                            new Internet(context).enable();
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

        //----------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void addDriverDialog()
    {
        final A9AddBinding binding1 = A9AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder1 = new AlertDialog.Builder(context);
        alertDialogBuilder1.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder1.setCancelable(false);
        alertDialogBuilder1.setPositiveButton("تایید", null);
        alertDialogBuilder1.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder1.create();

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
                            binding1.til1.setError("نام راننده را وارد کنید.");

                        else
                        {
                            String phone_number1 = "-";
                            String car_type1 = "-";
                            String number_plate1 = "-";

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1 = binding1.phoneNumber.getText().toString();

                            if (!binding1.carType.getText().toString().equals(""))
                                car_type1 = binding1.carType.getText().toString();

                            if (!binding1.numberPlate.getText().toString().equals(""))
                                number_plate1 = binding1.numberPlate.getText().toString();

                            if (new Internet(context).check())
                            {
                                createDriver(binding1.name.getText().toString(), phone_number1, car_type1, number_plate1, alertDialog);
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
                        alertDialogBuilder1 = null;
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

    public void createDriver(final String name, final String phone_number, final String car_type,final String number_plate, final AlertDialog alertDialog)
    {
        String url = getString(R.string.domain) + "api/driver/create";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("car_type", car_type);
            object.put("number_plate",number_plate);
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
                    realm.copyToRealmOrUpdate(new Activity9_DB(id, name, phone_number, car_type,number_plate,""));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "ایجاد راننده جدید با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialogBuilder1 = null;

                    addDriver();

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

    public void addDriver()
    {
        RealmResults<Activity9_DB> res = realm.where(Activity9_DB.class).findAll();

        models3.clear();

        for (int i=0;i<res.size();i++)
        {
            if (!res.get(i).getArchive().equals("done"))
                models3.add(new Activity9_Model(res.get(i).getId(), res.get(i).getName(), res.get(i).getPhone_number(), res.get(i).getCar_type(), res.get(i).getNumber_plate(), res.get(i).getArchive()));
        }

        adapter3.notifyDataSetChanged();
    }

}
