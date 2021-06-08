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
import com.example.boroodat.activity.Activity21_BuyerDetails;
import com.example.boroodat.activity.Activity8_Buyer;
import com.example.boroodat.databinding.A11ItemBinding;
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
import com.example.boroodat.model.activity11.Activity11_MainModel;
import com.example.boroodat.model.activity11.Activity11_ParentModel;
import com.example.boroodat.model.activity8.Activity8_MainModel;
import com.example.boroodat.model.activity8.Activity8_ParentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity11_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity11_ParentModel> models;
    private Context context;
    private RetryListener retryListener;

    public Activity11_Adapter(List<Activity11_ParentModel> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    public void setRetryListener(RetryListener retryListener)
    {
        this.retryListener=retryListener;
    }

    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A11ItemBinding binding;

        public mainViewHolder(A11ItemBinding binding)
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
        if (viewType == Activity11_ParentModel.Main)
        {
            A11ItemBinding binding = A11ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity11_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity11_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity11_ParentModel.NotFound)
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
            final Activity11_MainModel model = (Activity11_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.name.setText(model.getName());
            holder1.binding.phoneNumber.setText(model.getPhone_number());
            holder1.binding.destination.setText(model.getDestination());

            //-------------------------------------------------------------------------------------------------------

            holder1.binding.lnr1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context, Activity21_BuyerDetails.class);
                    intent.putExtra("buyer_id",model.getId());
                    intent.putExtra("buyer_name",model.getName());
                    intent.putExtra("phone_number",model.getPhone_number());
                    context.startActivity(intent);
                }
            });

            //-------------------------------------------------------------------------------------------------------
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
}
