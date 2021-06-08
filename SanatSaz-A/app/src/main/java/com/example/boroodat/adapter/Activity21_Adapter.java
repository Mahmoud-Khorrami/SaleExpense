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
import com.example.boroodat.activity.Activity10_EditSale;
import com.example.boroodat.databinding.A21ItemBinding;
import com.example.boroodat.databinding.A5ItemBinding;
import com.example.boroodat.databinding.DeleteDialog2Binding;
import com.example.boroodat.databinding.FragmentDetails1Binding;
import com.example.boroodat.databinding.LoadingBinding;
import com.example.boroodat.databinding.NotFoundBinding;
import com.example.boroodat.databinding.RetryBinding;
import com.example.boroodat.general.AppController;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.general.User_Info;
import com.example.boroodat.model.Fragment_Details1_Model;
import com.example.boroodat.model.activity21.Activity21_LoadingModel;
import com.example.boroodat.model.activity21.Activity21_MainModel;
import com.example.boroodat.model.activity21.Activity21_NotFoundModel;
import com.example.boroodat.model.activity21.Activity21_ParentModel;
import com.example.boroodat.model.activity21.Activity21_RetryModel;
import com.example.boroodat.model.activity5.Activity5_FragmentParentModel;
import com.example.boroodat.model.activity5.Activity5_FragmentsMainModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity21_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Activity21_ParentModel> models;
    private Context context;
    private DecimalFormat df = new DecimalFormat ( "#,###" );
    private FloatingActionButton select;
    private int counter = 0;

    public Activity21_Adapter(List<Activity21_ParentModel> models, Context context, FloatingActionButton select)
    {
        this.models = models;
        this.context = context;
        this.select = select;
    }


    public class mainViewHolder extends RecyclerView.ViewHolder
    {
        A21ItemBinding binding;

        public mainViewHolder(A21ItemBinding binding)
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
        if (viewType == Activity21_ParentModel.Main)
        {
            A21ItemBinding binding = A21ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new mainViewHolder(binding);
        }

        else if (viewType == Activity21_ParentModel.Loading)
        {
            LoadingBinding binding = LoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new loadingViewHolder(binding);
        }

        else if (viewType == Activity21_ParentModel.Retry)
        {
            RetryBinding binding = RetryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new retryViewHolder(binding);
        }

        else if (viewType == Activity21_ParentModel.NotFound)
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
            final Activity21_MainModel model = (Activity21_MainModel) models.get(position);
            final mainViewHolder holder1 = (mainViewHolder) holder;
            holder1.itemView.setTag(model);

            //--------------------------------------------------------------

            double remain = Double.parseDouble(model.getSum()) - Double.parseDouble(model.getPayment());
            holder1.binding.factorNumber.setText(model.getFactorNumber());
            holder1.binding.totalPrice.setText(df.format(Math.round(Double.parseDouble(model.getSum()))) + " ریال");
            holder1.binding.payment.setText(df.format(Math.round(Double.parseDouble(model.getPayment()))) + " ریال");
            holder1.binding.remain.setText(df.format(Math.round(remain)) + " ریال");
            holder1.binding.date.setText(model.getDate());
            holder1.binding.description.setText(model.getDetailsDescription());

            if (model.isSelect())
                holder1.binding.cardView.setChecked(true);
            //--------------------------------------------------------------

            holder1.binding.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (holder1.binding.cardView.isChecked())
                    {
                        model.setSelect(false);
                        holder1.binding.cardView.setChecked(false);
                        counter = counter-1 ;
                        if (counter == 0)
                            select.setVisibility(View.GONE);
                    }
                    else
                    {
                        if (select.getVisibility() == View.GONE)
                            select.setVisibility(View.VISIBLE);

                        model.setSelect(true);
                        holder1.binding.cardView.setChecked(true);
                        counter = counter+1;
                    }
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

    public void setCounter()
    {
        counter = 0 ;
    }

}
