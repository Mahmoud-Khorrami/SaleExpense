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
import com.example.boroodat.activity.Activity8_Buyer;
import com.example.boroodat.databinding.A8AddBinding;
import com.example.boroodat.databinding.A8ItemBinding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.interfaces.RetryListener;
import com.example.boroodat.model.activity8.Activity8_MainModel;
import com.example.boroodat.model.activity8.Activity8_ParentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity8_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity8_ParentModel> models;
    private Context context;
    private int from;
    private TextView txtName,txtId;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private String from2;
    private int s1=0;
    private RetryListener retryListener;

    public Activity8_Adapter(List<Activity8_ParentModel> models, Context context, int from, TextView txtName, TextView txtId, AlertDialog alertDialog, String from2)
    {
        this.models = models;
        this.context = context;
        this.from = from;
        this.txtName = txtName;
        this.txtId = txtId;
        this.alertDialog=alertDialog;
        this.from2 = from2;

        //------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);
    }

    public Activity8_Adapter(List<Activity8_ParentModel> models, Context context, int from, String from2)
    {
        this.models = models;
        this.context = context;
        this.from = from;
        this.from2 = from2;

        //------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(false);
    }

    public void setRetryListener(RetryListener retryListener)
    {
        this.retryListener=retryListener;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A8ItemBinding binding;

        public mainViewHolder(A8ItemBinding binding)
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
        if (viewType == Activity8_ParentModel.Main)
        {
            A8ItemBinding binding = A8ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity8_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity8_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity8_ParentModel.NotFound)
        {
            NotFoundBinding binding = NotFoundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new notFoundViewHolder(binding);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        holder.setIsRecyclable(false);

        if (holder instanceof mainViewHolder)
        {
            final Activity8_MainModel model = (Activity8_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.name.setText(model.getName());
            holder1.binding.phoneNumber.setText(model.getPhone_number());
            holder1.binding.destination.setText(model.getDestination());

            //-------------------------------------------------------------------------------------------------------

            if (model.isSelected())
                holder1.binding.fab.setVisibility(View.VISIBLE);
            else
                holder1.binding.fab.setVisibility(View.GONE);

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.lnr1.setOnLongClickListener(new View.OnLongClickListener()
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

                        ((Activity8_Buyer) context).changeStatusLnr3();
                        model.setSelected(!model.isSelected());

                        if (model.isSelected())
                            holder1.binding.fab.setVisibility(View.VISIBLE);
                        else
                            holder1.binding.fab.setVisibility(View.GONE);
                    }

                    return true;
                }
            });
            //-------------------------------------------------------------------------------------------------------

            holder1.binding.lnr1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (s1==0)
                    {
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
                            holder1.binding.fab.setVisibility(View.VISIBLE);
                        else
                            holder1.binding.fab.setVisibility(View.GONE);
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
                    if (retryListener != null)
                    {
                        retryListener.retry1();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    private void editDialog(final Activity8_MainModel model)
    {
        final A8AddBinding binding1 = A8AddBinding.inflate(LayoutInflater.from(context));
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
        binding1.destination.setText(model.getDestination());

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
                                edit(model,binding1.name.getText().toString(),phone_number1,destination1,alertDialog);
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

        //---------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void edit(final Activity8_MainModel model, final String name, final String phone_number, final String destination, final AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/buyer/edit";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("buyer_id", model.getId());
            object.put("name", name);
            object.put("phone_number",phone_number);
            object.put("destination", destination);
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
                        model.setName(name);
                        model.setPhone_number(phone_number);
                        model.setDestination(destination);
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    public void changeStatusS1()
    {
        s1=0;

        for (int i=0; i<models.size();i++)
        {
            if (models.get(i).getCurrentType() == Activity8_ParentModel.Main)
            {
                Activity8_MainModel model = (Activity8_MainModel) models.get(i);
                model.setSelected(false);
            }
        }

        notifyDataSetChanged();
    }
}
