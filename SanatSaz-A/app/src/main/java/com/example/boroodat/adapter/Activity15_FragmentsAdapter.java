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
import com.example.boroodat.activity.Activity17_EditExpense;
import com.example.boroodat.databinding.A15ItemBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.databinding.FragmentDetails1Binding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity15.Activity15_FragmentParentModel;
import com.example.boroodat.model.activity15.Activity15_FragmentsMainModel;
import com.example.boroodat.model.Fragment_Details1_Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity15_FragmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity15_FragmentParentModel> models;
    private List<Fragment_Details1_Model> models2= new ArrayList<>();
    private Fragment_details1_Adapter adapter2;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;
    private String from;

    public Activity15_FragmentsAdapter(List<Activity15_FragmentParentModel> models, Context context, String from)
    {
        this.models = models;
        this.context = context;
        this.from = from;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A15ItemBinding binding;

        public mainViewHolder(A15ItemBinding binding)
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
        if (viewType == Activity15_FragmentParentModel.Main)
        {
            A15ItemBinding binding = A15ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity15_FragmentParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity15_FragmentParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity15_FragmentParentModel.NotFound)
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
            final Activity15_FragmentsMainModel model = (Activity15_FragmentsMainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------
            progressDialog = new SpotsDialog(context,R.style.Custom);
            progressDialog.setCancelable(false);

            //--------------------------------------------------------------

            holder1.binding.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder1.binding.totalPrice));
            holder1.binding.factorNumber.setText(model.getFactorNumber());
            holder1.binding.totalPrice.setText(model.getSum());
            holder1.binding.date.setText(model.getDate());

            //--------------------------------------------------------------

            holder1.binding.more.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    details(model);
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
                    //getAccount();
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public void details(final Activity15_FragmentsMainModel model)
    {
        final FragmentDetails1Binding binding1 = FragmentDetails1Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //-------------------------------------------------------------------------------------------------------

        binding1.lnr1.setVisibility(View.GONE);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ویرایش", null);
        alertDialogBuilder.setNegativeButton("حذف",null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.sum.addTextChangedListener(new NumberTextWatcherForThousand(binding1.sum));
        binding1.payment.addTextChangedListener(new NumberTextWatcherForThousand(binding1.payment));
        binding1.remain.addTextChangedListener(new NumberTextWatcherForThousand(binding1.remain));

        final double remain = Double.parseDouble(model.getSum()) - Double.parseDouble(model.getPayment());
        //----------------------------------------------------------------------------------------------------------

            binding1.factorNumber.setText(model.getFactorNumber());
            binding1.date.setText(model.getDate());
            binding1.sum.setText(model.getSum());
            binding1.payment.setText(model.getPayment());
            binding1.remain.setText(Math.round(remain)+"");
            binding1.accountNumber.setText(model.getAccount_title());
            binding1.description.setText(model.getDescription());


        //--------------------------------------------------------------------------------------------

        adapter2 = new Fragment_details1_Adapter(models2);
        binding1.recyclerView.setLayoutManager ( new LinearLayoutManager( context ) );
        binding1.recyclerView.setAdapter (adapter2);

        try
        {
            JSONArray array = model.getDetails();

            models2.clear();

            for (int i=array.length()-1;i>=0;i--)
            {
                JSONObject object=array.getJSONObject(i);

                models2.add ( new Fragment_Details1_Model( Integer.parseInt(object.getString("id")),object.getString("row"),object.getString("description"),object.getString("number"),object.getString("unit_price"),object.getString("total_price")) );
            }
            adapter2.notifyDataSetChanged();

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

                        alertDialog.dismiss();
                        alertDialogBuilder = null;

                        Intent intent=new Intent(context, Activity17_EditExpense.class);
                        intent.putExtra("expense_id",model.getId());
                        intent.putExtra("factor_number",model.getFactorNumber());
                        intent.putExtra("date",model.getDate());
                        intent.putExtra("sum",model.getSum());
                        intent.putExtra("payment",model.getPayment());
                        intent.putExtra("remain",Math.round(remain)+"");
                        intent.putExtra("account_id",model.getAccount_id());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("account_title",model.getAccount_title());
                        intent.putExtra("from",from);

                        context.startActivity(intent);

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

    private void deleteDialog(final Activity15_FragmentsMainModel model)
    {
        final DeleteDialog2Binding binding1 = DeleteDialog2Binding.inflate(LayoutInflater.from(context));
        View view1 = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view1);

        //-------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

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

    private void delete(final Activity15_FragmentsMainModel model)
    {
        String url = context.getString(R.string.domain) + "api/expense/delete";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("expense_id",model.getId());
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
                int position = models.indexOf ( model );
                models.remove ( position );
                notifyItemRemoved ( position );

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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}
