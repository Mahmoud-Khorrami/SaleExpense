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
import com.example.boroodat.databinding.A13ItemBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.databinding.F2SalaryAddBinding;
import com.example.boroodat.databinding.FragmentDetails2Binding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.Account;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Date;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.Personnel;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity13.Activity13_FragmentMainModel;
import com.example.boroodat.model.activity13.Activity13_FragmentParentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity13_FragmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity13_FragmentParentModel> models;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder = null;
    private android.app.AlertDialog progressDialog;

    public Activity13_FragmentsAdapter(List<Activity13_FragmentParentModel> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A13ItemBinding binding;

        public mainViewHolder(A13ItemBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class loadingViewHolder extends RecyclerView.ViewHolder
    {
        private LoadingBinding binding;

        public loadingViewHolder(LoadingBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class retryViewHolder extends RecyclerView.ViewHolder
    {
        private RetryBinding binding;

        public retryViewHolder(RetryBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
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
        if (viewType == Activity13_FragmentParentModel.Main)
        {
            A13ItemBinding binding = A13ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity13_FragmentParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity13_FragmentParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity13_FragmentParentModel.NotFound)
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
            final Activity13_FragmentMainModel model = (Activity13_FragmentMainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //--------------------------------------------------------------
            progressDialog = new SpotsDialog(context, R.style.Custom);
            progressDialog.setCancelable(false);

            //--------------------------------------------------------------

            holder1.binding.salary.addTextChangedListener(new NumberTextWatcherForThousand(holder1.binding.salary));
            holder1.binding.earnest.addTextChangedListener(new NumberTextWatcherForThousand(holder1.binding.earnest));
            holder1.binding.insuranceTax.addTextChangedListener(new NumberTextWatcherForThousand(holder1.binding.insuranceTax));

            //--------------------------------------------------------------

            holder1.binding.name.setText(model.getPersonnel_name());
            holder1.binding.date.setText(model.getDate());
            holder1.binding.salary.setText(model.getSalary());
            holder1.binding.earnest.setText(model.getEarnest());
            holder1.binding.insuranceTax.setText(model.getInsurance_tax());

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
            holder1.binding.progressbar.setIndeterminate(true);
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


    public void details(final Activity13_FragmentMainModel model)
    {
        final FragmentDetails2Binding binding1 = FragmentDetails2Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //-------------------------------------------------------------------------------------------------------


        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ویرایش", null);
        alertDialogBuilder.setNegativeButton("حذف", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.salary.addTextChangedListener(new NumberTextWatcherForThousand(binding1.salary));
        binding1.earnest.addTextChangedListener(new NumberTextWatcherForThousand(binding1.earnest));
        binding1.insuranceTax.addTextChangedListener(new NumberTextWatcherForThousand(binding1.insuranceTax));

        //----------------------------------------------------------------------------------------------------------

        binding1.name.setText(model.getPersonnel_name());
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

    private void editDialog(final Activity13_FragmentMainModel model)
    {
        final F2SalaryAddBinding binding1 = F2SalaryAddBinding.inflate(LayoutInflater.from(context));
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
        binding1.personnelName.setText(model.getPersonnel_name());
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
                new Personnel(context).show(binding1.personnelName, binding1.personnelId);
            }
        });

        //----------------------------------------------------------------------------------------------------------

        binding1.accountTitle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Account(context, "manager").show(binding1.accountTitle, binding1.accountId);
            }
        });
        //----------------------------------------------------------------------------------------------------------

        binding1.date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Date(binding1.date, context).setDate();
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
                            binding1.scrollView.scrollTo(0, binding1.til1.getTop());
                            binding1.til1.setError("نام و نام خانوادگی را وارد کنید.");
                        }
                        else if (binding1.salary.getText().toString().equals("") && binding1.earnest.getText().toString().equals("") && binding1.insuranceTax.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0, binding1.til4.getTop());
                            Toast.makeText(context, "حداقل یک از آیتم های حقوق ، بیعانه ، بیمه و مالیات باید تکمیل شود.", Toast.LENGTH_LONG).show();
                        }
                        else if (binding1.accountTitle.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0, binding1.til2.getTop());
                            binding1.til2.setError("حساب بانکی را مشخص کنید.");
                        }
                        else if (binding1.date.getText().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0, binding1.til3.getTop());
                            binding1.til3.setError("تاریخ پرداخت را وارد کنید.");
                        }

                        else
                        {
                            String salary2 = "0";
                            String earnest2 = "0";
                            String insurance_tax2 = "0";
                            String description1 = "-";

                            if (!binding1.salary.getText().toString().equals(""))
                                salary2 = NumberTextWatcherForThousand.trimCommaOfString(binding1.salary.getText().toString());

                            if (!binding1.earnest.getText().toString().equals(""))
                                earnest2 = NumberTextWatcherForThousand.trimCommaOfString(binding1.earnest.getText().toString());

                            if (!binding1.insuranceTax.getText().toString().equals(""))
                                insurance_tax2 = NumberTextWatcherForThousand.trimCommaOfString(binding1.insuranceTax.getText().toString());


                            if (!binding1.description.getText().toString().equals(""))
                                description1 = binding1.description.getText().toString();

                            if (new Internet(context).check())
                            {
                                edit(model, binding1.personnelName.getText().toString(), binding1.personnelId.getText().toString(), salary2, earnest2, insurance_tax2, binding1.accountId.getText().toString(), binding1.accountTitle.getText().toString(), binding1.date.getText().toString(), description1, alertDialog);
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

    public void edit(final Activity13_FragmentMainModel model, final String name, final String personnel_id, final String salary, final String earnest, final String insurance_tax, final String account_id, final String account_title, final String date, final String description, final AlertDialog alertDialog)
    {
        final String url = context.getString(R.string.domain) + "api/salary/edit";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("id", model.getId());
            object.put("personnel_id", personnel_id);
            object.put("salary", salary);
            object.put("earnest", earnest);
            object.put("insurance_tax", insurance_tax);
            object.put("account_id", account_id);
            object.put("date", date);
            object.put("description", description);
            object.put("secret_key", context.getString(R.string.secret_key));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        final Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                model.setPersonnel_name(name);
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
                headers.put("Authorization", "Bearer " + new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void deleteDialog(final Activity13_FragmentMainModel model)
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

    private void delete(final Activity13_FragmentMainModel model)
    {
        String url = context.getString(R.string.domain) + "api/salary/delete";
        progressDialog.show();

        final JSONObject object = new JSONObject();
        try
        {
            object.put("id", model.getId());
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
                int position = models.indexOf(model);
                models.remove(position);
                notifyItemRemoved(position);
                alertDialogBuilder = null;
                notifyDataSetChanged();
                progressDialog.dismiss();
                Toast.makeText(context, "حذف آیتم با موفقیت انجام شد.", Toast.LENGTH_LONG).show();
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
                headers.put("Authorization", "Bearer " + new User_Info().token());
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(3000, 1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }
}