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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.database.Fragment4_DB;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.databinding.F2DepositAddBinding;
import com.example.boroodat.databinding.FragmentDetails3Binding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Fragment4_Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment4_Adapter extends RecyclerView.Adapter<Fragment4_Adapter.viewHolder>
{
    private List<Fragment4_Model> models;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder=null;
    private Realm realm;
    private android.app.AlertDialog progressDialog;
    private EditText tPrice;
    private String last_account_id,last_amount;

    public Fragment4_Adapter(List<Fragment4_Model> models, Context context, EditText tPrice)
    {
        this.models = models;
        this.context = context;
        this.tPrice=tPrice;
    }

    public static class viewHolder extends RecyclerView.ViewHolder
    {

        TextView title,date;
        EditText amount;
        ImageView more;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            more=itemView.findViewById(R.id.more);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.f4_item, parent, false );

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position)
    {
        holder.setIsRecyclable ( false );
        final Fragment4_Model model = models.get ( position );

        holder.itemView.setTag ( model );

        realm=Realm.getDefaultInstance();
        //--------------------------------------------------------------

        progressDialog = new SpotsDialog(context,R.style.Custom);
        progressDialog.setCancelable(false);

        //--------------------------------------------------------------

        holder.amount.addTextChangedListener(new NumberTextWatcherForThousand(holder.amount));

        holder.title.setText(model.getTitle());
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());

        //--------------------------------------------------------------

        holder.more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder == null)
                    getDeposit(model);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public void setFilter(List<Fragment4_Model> filter)
    {
        models=new ArrayList<>();
        models.addAll(filter);
        notifyDataSetChanged();

    }

    public void getDeposit(final Fragment4_Model model)
    {
        String url = context.getString(R.string.domain) + "api/deposit/get-deposit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("deposit_id",model.getId());
            object.put("secret_key",context.getString(R.string.secret_key));
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
                    if (response.getString("code").equals("200"))
                    {
                        details(response,model);
                    }
                    else
                        Toast.makeText(context,"کد "+response.getString("code")+": "+response.getString("message"),Toast.LENGTH_LONG).show();

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

    public void details(final JSONObject response, final Fragment4_Model model)
    {
        final FragmentDetails3Binding binding1 = FragmentDetails3Binding.inflate(LayoutInflater.from(context));
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

        binding1.amount.addTextChangedListener(new NumberTextWatcherForThousand(binding1.amount));

        //----------------------------------------------------------------------------------------------------------

        try
        {
            JSONObject object=response.getJSONObject("deposit");

            binding1.title.setText(object.getString("title"));
            binding1.amount.setText(last_amount);
            binding1.accountTitle.setText(response.getString("account_title"));
            binding1.date.setText(object.getString("date"));
            binding1.description.setText(object.getString("description"));

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

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
                        depositDialog(response,model);
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

    public void depositDialog(final JSONObject response, final Fragment4_Model model)
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

        binding1.amount.addTextChangedListener(new NumberTextWatcherForThousand(binding1.amount));

        //---------------------------------------------------------------------------------------------------

        binding1.title.addTextChangedListener(new ClearError(binding1.til1));
        binding1.amount.addTextChangedListener(new ClearError(binding1.til2));
        binding1.account.addTextChangedListener(new ClearError(binding1.til3));
        binding1.date.addTextChangedListener(new ClearError(binding1.til4));

        //---------------------------------------------------------------------------------------------------

        try
        {
            JSONObject object=response.getJSONObject("deposit");
            last_account_id=object.getString("account_id");
            last_amount=object.getString("amount");

            binding1.title.setText(object.getString("title"));
            binding1.amount.setText(last_amount);
            binding1.accountId.setText(last_account_id);
            binding1.account.setText(response.getString("account_title"));
            binding1.date.setText(object.getString("date"));
            binding1.description.setText(object.getString("description"));

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        //---------------------------------------------------------------------------------------------------

        binding1.account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,"manager").dialog(binding1.account,binding1.accountId);
            }
        });

        //---------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date,context).setDate();

            }
        });

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


                            if (new Internet(context).check())
                            {

                                editDeposit(model, binding1.title.getText().toString(), amount, binding1.accountId.getText().toString(), binding1.date.getText().toString(), binding1.description.getText().toString(), alertDialog);
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

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void editDeposit(final Fragment4_Model model, final String title, final String amount, final String account_id, final String date, final String description, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/deposit/edit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("deposit_id",model.getId());
            object.put("company_id",new User_Info().company_id());
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
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(new Fragment4_DB(model.getId(), title, amount,date,account_id ));
                realm.commitTransaction();

                //-------------------------------------------------------------------------------

                new Account().decrease(last_account_id,last_amount);
                new Account().increase(account_id,amount);

                new Report().deposit(last_amount,"d");
                new Report().deposit(amount,"i");
                //-------------------------------------------------------------------------------

                model.setTitle(title);
                model.setAmount(amount);
                model.setDate(date);
                notifyDataSetChanged();

                //--------------------------------------------------------------------------------

                double price=0;

                for (int i=0;i<models.size();i++)
                    price=price+Double.parseDouble(models.get(i).getAmount());

                tPrice.setText(Math.round(price)+"");

                //-----------------------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(context, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void deleteDialog(final Fragment4_Model model)
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

    private void delete(final Fragment4_Model model)
    {
        String url = context.getString(R.string.domain) + "api/deposit/delete";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("deposit_id",model.getId());
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

                realm.executeTransaction(new Realm.Transaction()
                {
                    @Override
                    public void execute(Realm realm)
                    {
                        RealmResults<Fragment4_DB> res = realm.where(Fragment4_DB.class)
                                .equalTo("id",model.getId())
                                .findAll();

                        if (res.size() > 0)
                            res.deleteAllFromRealm();
                    }
                });

                //-----------------------------------------------------------------------------------------

                new Account().decrease(model.getAccount_id(),model.getAmount());

                new Report().deposit(model.getAmount(),"d");
                //----------------------------------------------------------------------------

                int position = models.indexOf ( model );
                models.remove ( position );
                notifyItemRemoved ( position );

                //----------------------------------------------------------------------------

                double price=0;

                for (int i=0;i<models.size();i++)
                    price=price+Double.parseDouble(models.get(i).getAmount());

                tPrice.setText(Math.round(price)+"");

                //----------------------------------------------------------------------------

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
