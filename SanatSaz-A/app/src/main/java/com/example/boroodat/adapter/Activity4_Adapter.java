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
import android.widget.LinearLayout;
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
import com.example.boroodat.databinding.A4AddBinding;
import com.example.boroodat.databinding.A4ItemBinding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.ClearError;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity4.Activity4_LoadingModel;
import com.example.boroodat.model.activity4.Activity4_MainModel;
import com.example.boroodat.model.activity4.Activity4_ParentModel;
import com.example.boroodat.model.activity4.Activity4_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity4_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity4_ParentModel> models;
    private Context context;
    private AlertDialog progressDialog;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder=null;

    public Activity4_Adapter(List<Activity4_ParentModel> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A4ItemBinding binding;

        public mainViewHolder(A4ItemBinding binding)
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
        if (viewType == Activity4_ParentModel.Main)
        {
            A4ItemBinding binding = A4ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity4_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity4_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity4_ParentModel.NotFound)
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
            final Activity4_MainModel model = (Activity4_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------

            progressDialog = new SpotsDialog(context, R.style.Custom);
            progressDialog.setCancelable(false);

            //-------------------------------------------------------------------------------------------------------
            holder1.binding.name.setText(model.getName());
            holder1.binding.phoneNumber.setText(model.getPhone());
            holder1.binding.role.setText(model.getRole());

            //-------------------------------------------------------------------------------------------------------

            if (new User_Info().user_id().equals(model.getId()+""))
                holder1.binding.delete.setVisibility(View.GONE);

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
                    getUser();
                }
            });
        }

    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    private void editDialog(final Activity4_MainModel model)
    {
        final A4AddBinding binding1 = A4AddBinding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

        //----------------------------------------------------------------------------------------------------------

        binding1.name.addTextChangedListener(new ClearError(binding1.til1));
        binding1.password.addTextChangedListener(new ClearError(binding1.til2));
        binding1.phone.addTextChangedListener(new ClearError(binding1.til3));

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
                        {
                            binding1.scrollView.scrollTo(0,binding1.til1.getTop());
                            binding1.til1.setError("نام کاربر را وارد کنید.");
                        }

                        else if (binding1.password.getText().toString().length() < 8)
                        {
                            binding1.scrollView.scrollTo(0,binding1.til2.getTop());
                            binding1.til2.setError("رمز عبور باید حداقل 8 رقم باشد.");
                        }

                        else if (binding1.phone.getText().toString().length() != 11)
                        {
                            binding1.scrollView.scrollTo(0,binding1.til3.getTop());
                            binding1.til3.setError("شماره همراه کاربر باید 11 رقم باشد.");
                        }

                        else if (binding1.spinner.getSelectedItem().toString().equals(""))
                        {
                            binding1.scrollView.scrollTo(0,binding1.role.getTop());
                            Toast.makeText(context,"سمت کاربر را انتخاب کنید.",Toast.LENGTH_LONG).show();
                        }

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

        //------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg127));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void edit(final Activity4_MainModel model, final String name, final String password, final String phone_number, final String role, final androidx.appcompat.app.AlertDialog alertDialog)
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
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
        AppController.getInstance().addToRequestQueue(request);

    }

    private void archiveDialog(final Activity4_MainModel model)
    {
        final com.example.boroodat.databinding.DeleteDialog2Binding binding1 = com.example.boroodat.databinding.DeleteDialog2Binding.inflate(LayoutInflater.from(context));
        View view = binding1.getRoot();
        alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);

        //----------------------------------------------------------------------------------------------------------

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("تایید", null);
        alertDialogBuilder.setNeutralButton("لغو", null);
        final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

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

                        if (new Internet(context).check())
                            archive(model, alertDialog);
                        else
                            new Internet(context).enable();


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

        //------------------------------------------------------------------------------------------

        alertDialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bkg129));
        alertDialog.show();
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        width = (int) ((width) * ((double) 4 / 5));
        alertDialog.getWindow().setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void archive(final Activity4_MainModel model, final androidx.appcompat.app.AlertDialog alertDialog)
    {
        String url = context.getString(R.string.domain) + "api/user/archive";
        progressDialog.show();

        JSONObject object = new JSONObject();
        try
        {
            object.put("user_id",model.getId());
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

    public void getUser()
    {
        models.clear();
        models.add(new Activity4_LoadingModel());
        notifyDataSetChanged();

        String url = context.getString(R.string.domain) + "api/user/show";

        final JSONObject object = new JSONObject();
        try
        {
            object.put("company_id",new User_Info().company_id());
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
                models.clear();
                try
                {
                    JSONArray array = response.getJSONArray("users");

                    for (int i = array.length() - 1; i >= 0; i--)
                    {
                        JSONObject object1 = array.getJSONObject(i);

                        String id = object1.getString("id");
                        String name = object1.getString("name");
                        String phone = object1.getString("phone_number");
                        String role = object1.getString("role");


                        models.add(new Activity4_MainModel(id, name, phone, role));

                    }

                    notifyDataSetChanged();

                }
                catch (Exception e)
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
                models.add(new Activity4_RetryModel());
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
