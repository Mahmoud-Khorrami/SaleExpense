package com.example.boroodat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.boroodat.activity.Activity13_DriverDetails;
import com.example.boroodat.activity.Activity9_Driver;
import com.example.boroodat.database.Activity9_DB;
import com.example.boroodat.databinding.A9AddBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity9_Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;

public class Activity9_Adapter extends RecyclerView.Adapter<Activity9_Adapter.viewHolder>
{
    private List<Activity9_Model> models;
    private Context context;
    private int from;
    private TextView txtName,txtId;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private Realm realm;
    private String from2;
    public int s1=0;

    public Activity9_Adapter(List<Activity9_Model> models, Context context, int from, TextView txtName, TextView txtId, AlertDialog alertDialog,String from2)
    {
        this.models = models;
        this.context = context;
        this.from = from;
        this.txtName = txtName;
        this.txtId = txtId;
        this.alertDialog=alertDialog;
        this.from2 = from2;
    }

    public Activity9_Adapter(List<Activity9_Model> models, Context context, int from, String from2)
    {
        this.models = models;
        this.context = context;
        this.from = from;
        this.from2 = from2;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        public TextView name, phone_number, car_specs;
        public FloatingActionButton fab;
        public LinearLayout lnr1;
        public LinearLayout edit;

        public viewHolder(@NonNull View itemView)
        {
            super(itemView);

            name =itemView.findViewById(R.id.name);
            phone_number =itemView.findViewById(R.id.phoneNumber);
            car_specs =itemView.findViewById(R.id.carSpecs);
            fab =itemView.findViewById(R.id.fab);
            lnr1 =itemView.findViewById(R.id.lnr1);
            edit =itemView.findViewById(R.id.edit);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.a9_item, parent, false );
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position)
    {
        holder.setIsRecyclable(false);
        final Activity9_Model model = models.get(position);
        holder.itemView.setTag(model);

        realm=Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);

        //-------------------------------------------------------------------------------------------------------

        holder.name.setText(model.getName());
        holder.phone_number.setText(model.getPhone_number());
        holder.car_specs.setText(model.getCar_type() + "  " + model.getNumber_plate());

        //-------------------------------------------------------------------------------------------------------

        if (model.isSelected())
            holder.fab.setVisibility(View.VISIBLE);
        else
            holder.fab.setVisibility(View.GONE);

        if (model.getArchive().equals("done"))
            holder.edit.setVisibility(View.GONE);
        //-------------------------------------------------------------------------------------------------------

        holder.lnr1.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                if (from == 2 || from2.equals("user"))
                {

                }

                else
                {
                    s1 = 1;

                    if (!model.getArchive().equals("done"))
                        ((Activity9_Driver) context).changeStatusLnr3();

                    model.setSelected(!model.isSelected());

                    if (model.isSelected())
                        holder.fab.setVisibility(View.VISIBLE);
                    else
                        holder.fab.setVisibility(View.GONE);
                }

                return true;
            }
        });
        //-------------------------------------------------------------------------------------------------------

        holder.lnr1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (s1==0)

                {
                    if (from == 1)
                    {
                        if (new Internet(context).check())
                            getData5(model);
                        else
                            new Internet(context).enable();
                    }

                    if (from == 2)
                    {
                        txtName.setText(model.getName());
                        txtId.setText(model.getId() + "");
                        alertDialog.dismiss();
                    }
                }

                else
                {
                    model.setSelected(!model.isSelected());

                    if (model.isSelected())
                        holder.fab.setVisibility(View.VISIBLE);
                    else
                        holder.fab.setVisibility(View.GONE);
                }

            }
        });

        //-------------------------------------------------------------------------------------------------------

        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (alertDialogBuilder==null)
                    editDialog(model);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public void setFilter(List<Activity9_Model> filter)
    {
        models=new ArrayList<>();
        models.addAll(filter);
        notifyDataSetChanged();
    }

    private void editDialog(final Activity9_Model model)
    {
        final A9AddBinding binding1 = A9AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));

        //----------------------------------------------------------------------------------------------------------

        binding1.name.setText(model.getName());
        binding1.phoneNumber.setText(model.getPhone_number());
        binding1.carType.setText(model.getCar_type());
        binding1.numberPlate.setText(model.getNumber_plate());

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
                        if (binding1.name.getText().toString().equals(""))
                            binding1.til1.setError("نام راننده را وارد کنید.");

                        else
                        {
                            String phone_number1="-";
                            String car_type1="-";
                            String number_plate1="-";

                            if (!binding1.phoneNumber.getText().toString().equals(""))
                                phone_number1= binding1.phoneNumber.getText().toString();

                            if (!binding1.carType.getText().toString().equals(""))
                                car_type1=binding1.carType.getText().toString();

                            if (!binding1.numberPlate.getText().toString().equals(""))
                                number_plate1=binding1.numberPlate.getText().toString();

                            if (new Internet(context).check())
                            {
                                edit(model,binding1.name.getText().toString(),phone_number1,car_type1,number_plate1,alertDialog);
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

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void edit(final Activity9_Model model,final String name, final String phone_number, final String car_type,final String number_plate, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/driver/edit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("driver_id", model.getId());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("car_type", car_type);
            object.put("number_plate",number_plate);
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

                try
                {
                    if (response.getString("code").equals("200"))
                    {
                        realm.beginTransaction();
                    realm.copyToRealmOrUpdate(new Activity9_DB(model.getId(), name, phone_number, car_type,number_plate,""));
                    realm.commitTransaction();

                    //----------------------------------------------------

                    model.setName(name);
                    model.setPhone_number(phone_number);
                    model.setCar_type(car_type);
                    model.setNumber_plate(number_plate);
                    notifyDataSetChanged();

                    //----------------------------------------------------

                    progressDialog.dismiss();
                    Toast.makeText(context, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialogBuilder = null;
                    }

                    else
                    {
                        Toast.makeText(context, "کد " + response.getString("code") + ":" + response.getString("message"), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }

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

    public void getData5(final Activity9_Model model)
    {
        String url = context.getString(R.string.domain) + "api/general/data5";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
            object.put("driver_id",model.getId()+"");
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
                try
                {
                    JSONArray array = response.getJSONArray("sales");

                    boolean b1 = new SaveData(array).toFragment5DB();

                    if (b1)
                    {
                        Intent intent=new Intent(context, Activity13_DriverDetails.class);
                        intent.putExtra("driver_name",model.getName());
                        context.startActivity(intent);
                    }

                    else
                        Toast.makeText(context, "مجددا تلاش کنید.", Toast.LENGTH_LONG).show();
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

    public void changeStatusS1()
    {
        s1=0;

        for (int i=0; i<models.size();i++)
        {
            models.get(i).setSelected(false);
        }

        notifyDataSetChanged();
    }
}
