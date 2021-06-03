package com.example.boroodat.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.boroodat.databinding.A14AddBinding;
import com.example.boroodat.databinding.A14ItemBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity14_LoadingModel;
import com.example.boroodat.model.Activity14_MainModel;
import com.example.boroodat.model.Activity14_NotFoundModel;
import com.example.boroodat.model.Activity14_ParentModel;
import com.example.boroodat.model.Activity14_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity14_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity14_ParentModel> models;
    private Context context;
    private int from;
    private TextView txtName,txtId;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;

    public Activity14_Adapter(List<Activity14_ParentModel> models, Context context, int from, TextView txtName, TextView txtId, AlertDialog alertDialog)
    {
        this.models = models;
        this.context = context;
        this.from = from;
        this.txtName = txtName;
        this.txtId = txtId;
        this.alertDialog=alertDialog;
    }

    public Activity14_Adapter(List<Activity14_ParentModel> models, Context context, int from)
    {
        this.models = models;
        this.context = context;
        this.from = from;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A14ItemBinding binding;

        public mainViewHolder(A14ItemBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public class loadingViewHolder extends RecyclerView.ViewHolder
    {
        private LoadingBinding binding;

        public loadingViewHolder(LoadingBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public class retryViewHolder extends RecyclerView.ViewHolder
    {
        private RetryBinding binding;

        public retryViewHolder(RetryBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    public class notFoundViewHolder extends RecyclerView.ViewHolder
    {
        private NotFoundBinding binding;

        public notFoundViewHolder(NotFoundBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return models.get(position).getCurrentType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if (viewType == Activity14_ParentModel.Main)
        {
            A14ItemBinding binding = A14ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity14_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity14_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity14_ParentModel.NotFound)
        {
            NotFoundBinding binding = NotFoundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new notFoundViewHolder(binding);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        holder.setIsRecyclable(false);

        if (holder instanceof mainViewHolder)
        {
            final Activity14_MainModel model = (Activity14_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------

            progressDialog = new SpotsDialog(context, R.style.Custom);
            progressDialog.setCancelable(false);

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.name.setText(model.getName());
            holder1.binding.phoneNumber.setText(model.getPhone_number());
            holder1.binding.role.setText(model.getRole());

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.lnr2.setVisibility(View.GONE);

            holder1.binding.more.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (holder1.binding.lnr2.getVisibility()==View.VISIBLE)
                        holder1.binding.lnr2.setVisibility(View.GONE);
                    else
                        holder1.binding.lnr2.setVisibility(View.VISIBLE);
                }
            });

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.lnr1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (from==1)
                    {
                        if (new Internet(context).check())
                            {

                            }
                        else
                            new Internet(context).enable();
                    }

                    if (from==2)
                    {
                        txtName.setText(model.getName());
                        txtId.setText(model.getId()+"");
                        alertDialog.dismiss();
                    }

                }
            });

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (alertDialogBuilder==null)
                        editDialog(model);
                }
            });


            //-------------------------------------------------------------------------------------------------------

            holder1.binding.delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (alertDialogBuilder==null)
                        archiveDialog(model);
                }
            });
        }

        if (holder instanceof loadingViewHolder)
        {
            loadingViewHolder holder1 = (loadingViewHolder) holder;
            holder1.itemView.setTag(null);
            holder1.binding.progressbar.setIndeterminate(true );
        }

        if (holder instanceof retryViewHolder)
        {
            retryViewHolder holder1 = (retryViewHolder) holder;

            holder1.binding.retry.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    getPersonnel();
                }
            });
        }

    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    private void editDialog(final Activity14_MainModel model)
    {
        final A14AddBinding binding1 = A14AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.setText(model.getName());
        binding1.phoneNumber.setText(model.getPhone_number());
        binding1.registerDate.setText(model.getRegister_date());
        binding1.role.setText(model.getRole());
        binding1.creditCard.setText(model.getCredit_card());

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));
        binding1.phoneNumber.addTextChangedListener(new ClearError(binding1.til2));
        binding1.registerDate.addTextChangedListener(new ClearError(binding1.til3));

        //----------------------------------------------------------------------------------------------------------

        binding1.registerDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.registerDate,context).setDate();
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
                        if (binding1.name.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til1.getTop());
                            binding1.til1.setError("نام و نام خانوادگی را وارد کنید.");
                        }

                        else if (binding1.phoneNumber.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("شماره همراه را وارد کنید.");
                        }

                        else if (binding1.registerDate.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0, binding1.til3.getTop());
                            binding1.til3.setError("تاریخ عضویت را وارد کنید.");
                        }
                        else
                        {
                            String role1="-";
                            String credit_card1="-";

                            if (!binding1.role.getText().toString().equals(""))
                                role1=binding1.role.getText().toString();

                            if (!binding1.creditCard.getText().toString().equals(""))
                                credit_card1=binding1.creditCard.getText().toString();

                            if (new Internet(context).check())
                            {
                                edit(model,binding1.name.getText().toString(),binding1.phoneNumber.getText().toString(),binding1.registerDate.getText().toString(),role1,credit_card1,alertDialog);
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

    public void edit(final Activity14_MainModel model, final String name, final String phone_number, final String register_date, final String role, final String credit_card, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/personnel/edit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("personnel_id", model.getId());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("register_date",register_date);
            object.put("role", role);
            object.put("credit_card",credit_card);
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
                Toast.makeText(context, "ویرایش با موفقیت انجام شد.", Toast.LENGTH_SHORT).show();
                getPersonnel();
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

    private void archiveDialog(final Activity14_MainModel model)
    {
        final DeleteDialog2Binding binding1 = DeleteDialog2Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

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

                            if (new Internet(context).check())
                                archive(model,alertDialog);
                            else
                                new Internet(context).enable();
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

        //-------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg129));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void archive(final Activity14_MainModel model, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/personnel/archive";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("personnel_id",model.getId());
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
                        Toast.makeText(context,"حذف این فرد با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void getPersonnel()
    {
        models.clear();
        models.add(new Activity14_LoadingModel());
        notifyDataSetChanged();

        String url = context.getString(R.string.domain) + "api/personnel/personnel-query1";

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
                    String code = response.getString("code");

                    models.clear();

                    if (code.equals("200"))
                    {
                        JSONObject message = response.getJSONObject("message");
                        JSONArray result = message.getJSONArray("result");

                        for (int i=0; i<result.length(); i++)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity14_MainModel(object1.getString("id"),object1.getString("name"),object1.getString("phone_number"),object1.getString("register_date"),object1.getString("role"),object1.getString("credit_card"),object1.getString("exit_date")));
                        }

                        notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity14_NotFoundModel());
                        notifyDataSetChanged();
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

                models.clear();
                models.add(new Activity14_RetryModel());
                notifyDataSetChanged();

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
