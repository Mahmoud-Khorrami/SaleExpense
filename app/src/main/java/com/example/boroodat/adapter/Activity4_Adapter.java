package com.example.boroodat.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boroodat.R;
import com.example.boroodat.databinding.A4UserAddBinding;
import com.example.boroodat.databinding.DeleteDialog1Binding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.SaveData;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity4_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity4_Adapter extends RecyclerView.Adapter<Activity4_Adapter.viewHolder>
{
    private List<Activity4_Model> models;
    private Context context;
    private AlertDialog progressDialog;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder=null;

    public Activity4_Adapter(List<Activity4_Model> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        public TextView name,phoneNumber,role,edit,delete;
        public ImageView more;
        public LinearLayout lnr1,lnr2;

        public viewHolder(@NonNull View itemView)
        {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            phoneNumber=itemView.findViewById(R.id.phoneNumber);
            role=itemView.findViewById(R.id.role);
            more=itemView.findViewById(R.id.more);
            lnr1=itemView.findViewById(R.id.lnr1);
            lnr2=itemView.findViewById(R.id.lnr2);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.a4_item, parent, false );

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position)
    {
        holder.setIsRecyclable(false);
        final Activity4_Model model = models.get(position);
        holder.itemView.setTag(model);

        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);

        //----------------------------------------------------------------------------------------------------------

        holder.name.setText(model.getName());
        holder.phoneNumber.setText(model.getPhone());
        holder.role.setText(model.getRole());

        //-------------------------------------------------------------------------------------------------------

        if (new User_Info().user_id().equals(model.getId()+""))
            holder.delete.setVisibility(View.GONE);

        //-------------------------------------------------------------------------------------------------------

        holder.lnr2.setVisibility(View.GONE);

        holder.more.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (holder.lnr2.getVisibility()==View.VISIBLE)
                    holder.lnr2.setVisibility(View.GONE);
                else
                    holder.lnr2.setVisibility(View.VISIBLE);
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

        //-------------------------------------------------------------------------------------------------------

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

    private void editDialog(final Activity4_Model model)
    {
        final A4UserAddBinding binding1 = A4UserAddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.title.setText("ویرایش کاربر");

        //----------------------------------------------------------------------------------------------------------

        binding1.name.setText(model.getName());
        binding1.phone.setText(model.getPhone());

        //----------------------------------------------------------------------------------------------------------

        ArrayList<String> roles=new ArrayList<>();
        roles.add("");
        roles.add("مدیر");
        roles.add("کارمند");

        ArrayAdapter<String> adp = new ArrayAdapter<String>(context, R.layout.spinner_item, roles);
        binding1.spinner.setAdapter(adp);
        binding1.spinner.setSelection(0);

        //----------------------------------------------------------------------------------------------------------

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
                        if (binding1.name.getText().toString().equals(""))
                            binding1.name.setError("نام کاربر را وارد کنید.");

                        else if (binding1.password.getText().toString().length() < 8)
                            binding1.password.setError("رمز عبور باید حداقل 8 رقم باشد.");

                        else if (binding1.phone.getText().toString().length() != 11)
                            binding1.phone.setError("شماره همراه کاربر باید 11 رقم باشد.");

                        else if (binding1.spinner.getSelectedItem().toString().equals(""))
                            binding1.role.setError("سمت کاربر را انتخاب کنید.");

                        else
                        {
                            edit(model,binding1.name.getText().toString(),binding1.password.getText().toString(),binding1.phone.getText().toString(),binding1.spinner.getSelectedItem().toString(),alertDialog);
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

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void edit(final Activity4_Model model, final String name, final String password, final String phone_number, final String role, final androidx.appcompat.app.AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/user/edit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id",model.getId());
            object.put("name", name);
            object.put("password", password);
            object.put("phone_number", phone_number);
            object.put("role", role);
            object.put("company_id", new User_Info().company_id());
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
                    String code = response.getString("code");

                    if (code.equals("200"))
                    {
                        model.setName(name);
                        model.setPhone(phone_number);
                        model.setRole(role);

                        //------------------------------------------------------------------------------------------

                        Toast.makeText(context, "ویرایش با موفقیت انجام شد." , Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                    }

                    else
                        Toast.makeText(context,"کد " + response.getString("code") + " :" + response.getString("message"), Toast.LENGTH_LONG).show();

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

    private void deleteDialog(final Activity4_Model model)
    {
        final DeleteDialog1Binding binding1 = DeleteDialog1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.text.setText(context.getString(R.string.user_delete));

        //----------------------------------------------------------------------------------------------------------

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
                        if (binding1.password.getText().toString().equals(""))
                            binding1.password.setError("رمز عبور را وارد کنید.");

                        else
                        {
                            if (new Internet(context).check())
                                delete(model,binding1.password.getText().toString(),alertDialog);
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

        //....................................................................................................

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rounded_linear));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void delete(final Activity4_Model model, final String password, final androidx.appcompat.app.AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/user/delete";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id",model.getId());
            object.put("password", password);
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
                    if (response.getString("code").equals("200"))
                    {
                        int position = models.indexOf ( model );
                        models.remove ( position );
                        notifyItemRemoved ( position );
                        notifyDataSetChanged();
                        alertDialog.dismiss();
                        alertDialogBuilder = null;
                        Toast.makeText(context,"حذف این کاربر با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                        getData10();
                    }

                    else
                    {
                        Toast.makeText(context, "کد " + response.getString("code") + ":" + response.getString("message"), Toast.LENGTH_LONG).show();
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

    public void getData10()
    {
        String url = context.getString(R.string.domain) + "api/general/data10";

        JSONObject object = new JSONObject();
        try
        {
            object.put("company_id", new User_Info().company_id());
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
                    JSONArray array1 = response.getJSONArray("accounts");
                    JSONArray array2 = response.getJSONArray("reports");

                    new SaveData(array1).toActivity7DB();
                    new SaveData(array2).toReportDB();

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
}
