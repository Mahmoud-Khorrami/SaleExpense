package com.example.boroodat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.database.Activity14_DB;
import com.example.boroodat.database.Fragment7_DB;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.databinding.F1SalaryAddBinding;
import com.example.boroodat.databinding.FragmentDetails2Binding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Personnel;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity14_Model;
import com.example.boroodat.model.Activity7_Model;
import com.example.boroodat.model.Fragment7_Model;

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

public class Fragment7_Adapter extends RecyclerView.Adapter<Fragment7_Adapter.viewHolder>
{
    private List<Fragment7_Model> models;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Realm realm;
    private EditText txtSalary,txtEarnest,txtInsurance_tax,txtSum;


    public Fragment7_Adapter(Context context, List<Fragment7_Model> models, EditText salary, EditText earnest, EditText insurance_tax, EditText sum)
    {
        this.context = context;
        this.models = models;
        this.txtSalary = salary;
        this.txtEarnest = earnest;
        this.txtInsurance_tax = insurance_tax;
        this.txtSum = sum;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,date;
        public EditText salary,earnest,insurance_tax;
        public ImageView more;

        public viewHolder(@NonNull View itemView)
        {
            super(itemView);

            name =itemView.findViewById(R.id.name);
            date =itemView.findViewById(R.id.date);
            salary =itemView.findViewById(R.id.salary);
            earnest =itemView.findViewById(R.id.earnest);
            insurance_tax =itemView.findViewById(R.id.insuranceTax);
            more=itemView.findViewById(R.id.more);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.f7_item, parent, false );

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position)
    {
        holder.setIsRecyclable(false);
        final Fragment7_Model model = models.get(position);
        holder.itemView.setTag(model);

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);

        //-------------------------------------------------------------------------------------------------------

        holder.salary.addTextChangedListener(new NumberTextWatcherForThousand(holder.salary));
        holder.earnest.addTextChangedListener(new NumberTextWatcherForThousand(holder.earnest));
        holder.insurance_tax.addTextChangedListener(new NumberTextWatcherForThousand(holder.insurance_tax));

        //-------------------------------------------------------------------------------------------------------

        holder.name.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.salary.setText(model.getSalary());
        holder.earnest.setText(model.getEarnest());
        holder.insurance_tax.setText(model.getInsurance_tax());

        //-------------------------------------------------------------------------------------------------------

        holder.more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder == null)
                    details(model);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public void setFilter(List<Fragment7_Model> filter)
    {
        models=new ArrayList<>();
        models.addAll(filter);
        notifyDataSetChanged();

    }

    public void details(final Fragment7_Model model)
    {
        final FragmentDetails2Binding binding1 = FragmentDetails2Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //-------------------------------------------------------------------------------------------------------


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ویرایش", null);
        alertDialogBuilder.setNegativeButton("حذف",null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.salary.addTextChangedListener(new NumberTextWatcherForThousand(binding1.salary));
        binding1.earnest.addTextChangedListener(new NumberTextWatcherForThousand(binding1.earnest));
        binding1.insuranceTax.addTextChangedListener(new NumberTextWatcherForThousand(binding1.insuranceTax));

        //----------------------------------------------------------------------------------------------------------

        binding1.name.setText(model.getName());
        binding1.salary.setText(model.getSalary());
        binding1.earnest.setText(model.getEarnest());
        binding1.insuranceTax.setText(model.getInsurance_tax());
        binding1.accountTitle.setText(model.getAccount_title());
        binding1.date.setText(model.getDate());
        binding1.description.setText(model.getDescription());

        //-------------------------------------------------------------------------------------------------------

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            @Override
            public void onShow(DialogInterface dialogInterface)
            {
                Button edit = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                edit.setTextColor(context.getResources().getColor(R.color.black));
                edit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        editDialog(model);
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }
                });

                final Button delete = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                delete.setTextColor(context.getResources().getColor(R.color.black));
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        deleteDialog(model);
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
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
                        alertDialogBuilder = null;
                    }
                });
            }
        });

        //---------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawableResource(R.color.blue1);
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 9 / 10));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void editDialog(final Fragment7_Model model)
    {
        final F1SalaryAddBinding binding1 = F1SalaryAddBinding.inflate(LayoutInflater.from(context));
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

        binding1.personnelId.setText(model.getPersonnel_id());
        binding1.personnelName.setText(model.getName());
        binding1.salary.setText(model.getSalary());
        binding1.earnest.setText(model.getEarnest());
        binding1.insuranceTax.setText(model.getInsurance_tax());
        binding1.accountId.setText(model.getAccount_id());
        binding1.accountTitle.setText(model.getAccount_title());
        binding1.date.setText(model.getDate());
        binding1.description.setText(model.getDescription());

        //----------------------------------------------------------------------------------------------------------

        binding1.personnelName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Personnel(context).dialog(binding1.personnelName,binding1.personnelId);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.accountTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,"manager").dialog(binding1.accountTitle,binding1.accountId);
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
                            String salary2="0";
                            String earnest2="0";
                            String insurance_tax2="0";
                            String description1="-";

                            if (!binding1.salary.getText().toString().equals(""))
                                salary2=NumberTextWatcherForThousand.trimCommaOfString(binding1.salary.getText().toString());

                            if (!binding1.earnest.getText().toString().equals(""))
                                earnest2=NumberTextWatcherForThousand.trimCommaOfString(binding1.earnest.getText().toString());

                            if (!binding1.insuranceTax.getText().toString().equals(""))
                                insurance_tax2=NumberTextWatcherForThousand.trimCommaOfString(binding1.insuranceTax.getText().toString());


                            if (!binding1.description.getText().toString().equals(""))
                                description1=binding1.description.getText().toString();

                            if (new Internet(context).check())
                            {
                                edit(model,binding1.personnelName.getText().toString(),binding1.personnelId.getText().toString(),salary2,earnest2,insurance_tax2,binding1.accountId.getText().toString(),binding1.accountTitle.getText().toString(),binding1.date.getText().toString(),description1,alertDialog);
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

        //---------------------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void edit(final Fragment7_Model model, final String name, final String personnel_id, final String salary, final String earnest, final String insurance_tax, final String account_id, final String account_title, final String date, final String description, final AlertDialog alertDialog)
    {
        final String url = context.getString(R.string.domain) + "api/salary/edit";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("id",model.getId());
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

        final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment7_DB(model.getId(), name,personnel_id, salary, earnest, insurance_tax,account_id,account_title, date,description));
                realm.commitTransaction();

                //-------------------------------------------------------------------------------

                String last_amount=Double.parseDouble(model.getSalary())+Double.parseDouble(model.getEarnest())+Double.parseDouble(model.getInsurance_tax())+"";
                new Account().increase(model.getAccount_id(), last_amount);
                new Report().salary(last_amount,"d");

                String amount=Double.parseDouble(salary)+Double.parseDouble(earnest)+Double.parseDouble(insurance_tax)+"";
                new Account().decrease(account_id, amount);
                new Report().salary(amount,"i");


                //-------------------------------------------------------------------------------

                model.setName(name);
                model.setPersonnel_id(personnel_id);
                model.setSalary(salary);
                model.setEarnest(earnest);
                model.setInsurance_tax(insurance_tax);
                model.setAccount_id(account_id);
                model.setAccount_title(account_title);
                model.setDate(date);
                model.setDescription(description);
                notifyDataSetChanged();

                //-------------------------------------------------------------------------------

                double salary1=0;
                double earnest1=0;
                double insurance_tax1=0;

                for (int i=models.size()-1;i>=0;i--)
                {
                    salary1 = salary1 + Double.parseDouble(models.get(i).getSalary());
                    earnest1 = earnest1 + Double.parseDouble(models.get(i).getEarnest());
                    insurance_tax1 = insurance_tax1 + Double.parseDouble(models.get(i).getInsurance_tax());
                }

                txtSalary.setText(Math.round(salary1)+"");
                txtEarnest.setText(Math.round(earnest1)+"");
                txtInsurance_tax.setText(Math.round(insurance_tax1)+"");
                txtSum.setText(Math.round(salary1+earnest1+insurance_tax1)+"");

                //-------------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(context, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void deleteDialog(final Fragment7_Model model)
    {
        final Dialog1Binding binding1 = Dialog1Binding.inflate(LayoutInflater.from(context));
        View view1 = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view1);

        //-------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //-------------------------------------------------------------------------------------------------------

        binding1.message.setText("آیا می خواهید این آیتم را حذف کنید؟");

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
                        alertDialogBuilder = null;
                        alertDialog.dismiss();

                        if (new Internet(context).check())
                            delete(model);
                        else
                            new Internet(context).enable();
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
                        alertDialogBuilder = null;
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

    private void delete(final Fragment7_Model model)
    {
        String url = context.getString(R.string.domain) + "api/salary/delete";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("id",model.getId());
            object.put("secret_key", context.getString(R.string.secret_key));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                //----------------------------------------------------------------------------

                realm.executeTransaction(new Realm.Transaction()
                {
                    @Override
                    public void execute(Realm realm)
                    {
                        RealmResults<Fragment7_DB> res = realm.where(Fragment7_DB.class)
                                .equalTo("id",model.getId())
                                .findAll();

                        if (res.size() > 0)
                            res.deleteAllFromRealm();
                    }
                });

                //----------------------------------------------------------------------------

                String amount=Double.parseDouble(model.getSalary())+Double.parseDouble(model.getEarnest())+Double.parseDouble(model.getInsurance_tax())+"";
                new Account().increase(model.getAccount_id(), amount);
                new Report().salary(amount,"d");

                //----------------------------------------------------------------------------

                int position = models.indexOf ( model );
                models.remove ( position );
                notifyItemRemoved ( position );

                //----------------------------------------------------------------------------

                double salary1=0;
                double earnest1=0;
                double insurance_tax1=0;

                for (int i=models.size()-1;i>=0;i--)
                {
                    salary1 = salary1 + Double.parseDouble(models.get(i).getSalary());
                    earnest1 = earnest1 + Double.parseDouble(models.get(i).getEarnest());
                    insurance_tax1 = insurance_tax1 + Double.parseDouble(models.get(i).getInsurance_tax());
                }

                txtSalary.setText(Math.round(salary1)+"");
                txtEarnest.setText(Math.round(earnest1)+"");
                txtInsurance_tax.setText(Math.round(insurance_tax1)+"");
                txtSum.setText(Math.round(salary1+earnest1+insurance_tax1)+"");

                //-------------------------------------------------------------------------------

                alertDialogBuilder=null;
                notifyDataSetChanged ();

                progressDialog.dismiss();
                Toast.makeText(context,"حذف آیتم با موفقیت انجام شد.",Toast.LENGTH_LONG).show();

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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
