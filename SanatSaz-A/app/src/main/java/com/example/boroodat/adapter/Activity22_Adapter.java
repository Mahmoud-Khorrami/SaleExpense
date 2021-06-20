package com.example.boroodat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.boroodat.database.DefaultItems_DB;
import com.example.boroodat.databinding.A22ItemBinding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.activity22.Activity22_LoadingModel;
import com.example.boroodat.model.activity22.Activity22_MainModel;
import com.example.boroodat.model.activity22.Activity22_NotFoundModel;
import com.example.boroodat.model.activity22.Activity22_ParentModel;
import com.example.boroodat.model.activity22.Activity22_RetryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class Activity22_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity22_ParentModel> models;
    private Context context;
    private String type;
    private AlertDialog alertDialog;
    private Realm realm;
    private TextView default_name;

    public Activity22_Adapter(List<Activity22_ParentModel> models, Context context, String type, AlertDialog alertDialog, TextView default_name)
    {
        this.models = models;
        this.context = context;
        this.type = type;
        this.alertDialog = alertDialog;
        this.default_name = default_name;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A22ItemBinding binding;

        public mainViewHolder(A22ItemBinding binding)
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
        if (viewType == Activity22_ParentModel.Main)
        {
            A22ItemBinding binding = A22ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity22_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity22_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity22_ParentModel.NotFound)
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
            final Activity22_MainModel model = (Activity22_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            realm = Realm.getDefaultInstance();
            //--------------------------------------------------------------

            holder1.binding.name.setText(model.getName());

            //--------------------------------------------------------------

            holder1.binding.name.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    realm.executeTransaction(new Realm.Transaction()
                    {
                        @Override
                        public void execute(Realm realm)
                        {

                            RealmResults<DefaultItems_DB> results = realm.where(DefaultItems_DB.class)
                                    .equalTo("id",new User_Info().company_id())
                                    .findAll();

                            if (results.size()>0)
                            {
                                if (type.equals("account"))
                                {
                                    results.get(0).setAccount_title(model.getName());
                                    results.get(0).setAccount_id(model.getId());
                                }


                                if (type.equals("buyer"))
                                {
                                    results.get(0).setBuyer_name(model.getName());
                                    results.get(0).setBuyer_id(model.getId());
                                }

                                if (type.equals("driver"))
                                {
                                    results.get(0).setDriver_name(model.getName());
                                    results.get(0).setDriver_id(model.getId());
                                }


                                if (type.equals("seller"))
                                {
                                    results.get(0).setSeller_name(model.getName());
                                    results.get(0).setSeller_id(model.getId());
                                }

                                default_name.setText(model.getName());
                                alertDialog.dismiss();
                            }
                        }
                    });

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
                    if (type.equals("account"))
                        getAccount();
                    if (type.equals("buyer"))
                        getBuyer();
                    if (type.equals("driver"))
                        getDrivers();

                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    private void getAccount()
    {
        models.clear();
        models.add(new Activity22_LoadingModel());
        notifyDataSetChanged();

        String url = context.getString(R.string.domain) + "api/account/account-query1";

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

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("title")));
                        }

                        notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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

    private void getBuyer()
    {
        String url = context.getString(R.string.domain) + "api/buyer/get-buyers";

        models.clear();
        models.add(new Activity22_LoadingModel());
        notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("paginate",50);
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
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("name")));
                        }

                        notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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

    private void getDrivers()
    {
        String url = context.getString(R.string.domain) + "api/driver/get-drivers";

        models.clear();
        models.add(new Activity22_LoadingModel());
        notifyDataSetChanged();

        JSONObject object = new JSONObject();
        try
        {
            object.put("paginate",50);
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
                        JSONArray result = response.getJSONArray("result");

                        for (int i=result.length()-1; i>=0; i--)
                        {
                            JSONObject object1 = result.getJSONObject(i);

                            models.add(new Activity22_MainModel(object1.getString("id"),object1.getString("name")));
                        }

                        notifyDataSetChanged();
                    }

                    else if (code.equals("207"))
                    {
                        models.add(new Activity22_NotFoundModel());
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
                models.add(new Activity22_RetryModel());
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
