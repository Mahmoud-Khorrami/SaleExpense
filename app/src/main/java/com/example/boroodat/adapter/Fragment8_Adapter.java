package com.example.boroodat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.boroodat.activity.Activity19_EditMaterial;
import com.example.boroodat.database.Activity18_DB;
import com.example.boroodat.database.Fragment8_DB;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Report;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Fragment8_Model;

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

public class Fragment8_Adapter extends RecyclerView.Adapter<Fragment8_Adapter.viewHolder>
{
    private List<Fragment8_Model> models;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder=null;
    private Realm realm;
    private android.app.AlertDialog progressDialog;
    private EditText tPrice,payment,remain;
    private String from;

    public Fragment8_Adapter(List<Fragment8_Model> models, Context context, EditText tPrice, EditText payment, EditText remain,String from)
    {
        this.models = models;
        this.context = context;
        this.tPrice = tPrice;
        this.payment = payment;
        this.remain = remain;
        this.from=from;
    }

    public static class viewHolder extends RecyclerView.ViewHolder
    {

        TextView factorNumber,date,edit,delete;
        EditText totalPrice;
        LinearLayout lnr1,lnr2;
        ImageView more;

        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            factorNumber=itemView.findViewById(R.id.factorNumber);
            date=itemView.findViewById(R.id.date);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            totalPrice=itemView.findViewById(R.id.totalPrice);
            lnr1=itemView.findViewById(R.id.lnr1);
            lnr2=itemView.findViewById(R.id.lnr2);
            more=itemView.findViewById(R.id.more);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.f8_item, parent, false );

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position)
    {
        holder.setIsRecyclable ( false );
        final Fragment8_Model model = models.get ( position );

        holder.itemView.setTag ( model );

        realm=Realm.getDefaultInstance();
        //--------------------------------------------------------------

        progressDialog = new SpotsDialog(context,R.style.Custom);
        progressDialog.setCancelable(false);

        //--------------------------------------------------------------

        holder.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder.totalPrice));
        holder.factorNumber.setText(model.getFactorNumber());
        holder.totalPrice.setText(model.getSum());
        holder.date.setText(model.getDate());

        //--------------------------------------------------------------

        holder.lnr2.setVisibility(View.GONE);

        holder.more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (holder.lnr2.getVisibility()==View.GONE)
                    holder.lnr2.setVisibility(View.VISIBLE);
                else
                    holder.lnr2.setVisibility(View.GONE);
            }
        });


        //--------------------------------------------------------------

        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder==null)
                    getMaterial(model);
            }
        });

        //--------------------------------------------------------------

        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder==null)
                    deleteDialog(model);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public void setFilter(List<Fragment8_Model> filter)
    {
        models=new ArrayList<>();
        models.addAll(filter);
        notifyDataSetChanged();

    }

    public void getMaterial(final Fragment8_Model model)
    {
        String url = context.getString(R.string.domain) + "api/material/get-material";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("material_id",model.getId());
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
                        saveMaterial(response);
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

    public void saveMaterial(JSONObject response)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                RealmResults<Activity18_DB> res = realm.where(Activity18_DB.class).findAll();

                if (res.size() > 0)
                    res.deleteAllFromRealm();
            }
        });

        //---------------------------------------------------------------------------

        try
        {
            JSONArray array=response.getJSONArray("material_details");

            for (int i=array.length()-1;i>=0;i--)
            {
                JSONObject object=array.getJSONObject(i);

                realm.beginTransaction ();
                realm.copyToRealmOrUpdate ( new Activity18_DB( Integer.parseInt(object.getString("id")),object.getString("row"),object.getString("description"),object.getString("number"),object.getString("unit_price"),object.getString("total_price")) );
                realm.commitTransaction ();
            }

            //---------------------------------------------------------------------------

            JSONObject material=response.getJSONObject("material");

            Intent intent=new Intent(context, Activity19_EditMaterial.class);
            intent.putExtra("material_id",material.getString("id"));
            intent.putExtra("factor_number",material.getString("factor_number"));
            intent.putExtra("date",material.getString("date"));
            intent.putExtra("sum",material.getString("sum"));
            intent.putExtra("payment",material.getString("payment"));
            intent.putExtra("remain",material.getString("remain"));
            intent.putExtra("account_id",material.getString("account_id"));
            intent.putExtra("description",material.getString("description"));
            intent.putExtra("account_title",response.getString("account_title"));
            intent.putExtra("from",from);

            context.startActivity(intent);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }


    }

    private void deleteDialog(final Fragment8_Model model)
    {
        alertDialogBuilder = new AlertDialog.Builder ( context );

        alertDialogBuilder.setMessage ( "آیا می خواهید این آیتم را حذف کنید؟" );
        alertDialogBuilder.setCancelable ( false );

        alertDialogBuilder.setPositiveButton ( "تایید", new DialogInterface.OnClickListener ()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (new Internet(context).check())
                    delete(model);
                else
                    new Internet(context).enable();
            }
        } );

        alertDialogBuilder.setNeutralButton ( "لغو", new DialogInterface.OnClickListener ()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                alertDialogBuilder=null;
                dialog.cancel ();
            }
        } );

        alertDialogBuilder.create ().show ();

    }

    private void delete(final Fragment8_Model model)
    {
        String url = context.getString(R.string.domain) + "api/material/delete";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("material_id",model.getId());
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
                        RealmResults<Fragment8_DB> res = realm.where(Fragment8_DB.class)
                                .equalTo("id",model.getId())
                                .findAll();

                        if (res.size() > 0)
                            res.deleteAllFromRealm();
                    }
                });

                //----------------------------------------------------------------------------

                new Account().increase(model.getAccount_id(),model.getPayment());

                new Report().material(model.getSum(),model.getPayment(),"d");
                //----------------------------------------------------------------------------

                int position = models.indexOf ( model );
                models.remove ( position );
                notifyItemRemoved ( position );

                //----------------------------------------------------------------------------

                double price=0;
                double payment1=0;

                for (int i=0;i<models.size();i++)
                {
                    price = price + Double.parseDouble(models.get(i).getSum());
                    payment1=payment1+Double.parseDouble(models.get(i).getPayment());
                }

                tPrice.setText(Math.round(price)+"");
                payment.setText(Math.round(payment1)+"");
                remain.setText(Math.round(price-payment1)+"");

                //----------------------------------------------------------------------------

                progressDialog.dismiss();
                Toast.makeText(context,"حذف آیتم با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                alertDialogBuilder=null;
                notifyDataSetChanged ();
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
