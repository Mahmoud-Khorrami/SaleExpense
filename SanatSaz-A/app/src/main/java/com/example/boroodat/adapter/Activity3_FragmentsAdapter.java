package com.example.boroodat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.example.boroodat.databinding.A3ItemBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.databinding.F2DepositAddBinding;
import com.example.boroodat.databinding.FragmentDetails3Binding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Activity3_FragmentMainModel;
import com.example.boroodat.model.Activity3_FragmentParentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity3_FragmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity3_FragmentParentModel> models;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder=null;
    private android.app.AlertDialog progressDialog;

    public Activity3_FragmentsAdapter(List<Activity3_FragmentParentModel> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A3ItemBinding binding;

        public mainViewHolder(A3ItemBinding binding)
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
        if (viewType == Activity3_FragmentParentModel.Main)
        {
            A3ItemBinding binding = A3ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity3_FragmentParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity3_FragmentParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity3_FragmentParentModel.NotFound)
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
            final Activity3_FragmentMainModel model = (Activity3_FragmentMainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------
            progressDialog = new SpotsDialog(context,R.style.Custom);
            progressDialog.setCancelable(false);

            //--------------------------------------------------------------

            holder1.binding.amount.addTextChangedListener(new NumberTextWatcherForThousand(holder1.binding.amount));
            holder1.binding.amount.setText(model.getAmount());
            holder1.binding.title.setText(model.getTitle());
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

    public void details( final Activity3_FragmentMainModel model)
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

        binding1.title.setText(model.getTitle());
        binding1.amount.setText(model.getAmount());
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
                        depositDialog(model);
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

    public void depositDialog(final Activity3_FragmentMainModel model)
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

        binding1.title.setText(model.getTitle());
        binding1.amount.setText(model.getAmount());
        binding1.accountId.setText(model.getAccount_id());
        binding1.account.setText(model.getAccount_title());
        binding1.date.setText(model.getDate());
        binding1.description.setText(model.getDescription());

        //---------------------------------------------------------------------------------------------------

        binding1.account.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context,"manager").show(binding1.account,binding1.accountId);
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

                                editDeposit(model, binding1.title.getText().toString(), amount,binding1.account.getText().toString(), binding1.accountId.getText().toString(), binding1.date.getText().toString(), binding1.description.getText().toString(), alertDialog);
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

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void editDeposit(final Activity3_FragmentMainModel model, final String title, final String amount, final String account_title, final String account_id, final String date, final String description, final AlertDialog alertDialog)
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
                model.setTitle(title);
                model.setAmount(amount);
                model.setDate(date);
                model.setAccount_id(account_id);
                model.setAccount_title(account_title);
                model.setDescription(description);
                notifyDataSetChanged();

                //--------------------------------------------------------------------------------

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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void deleteDialog(final Activity3_FragmentMainModel model)
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

    private void delete(final Activity3_FragmentMainModel model)
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

                int position = models.indexOf ( model );
                models.remove ( position );
                notifyItemRemoved ( position );
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
