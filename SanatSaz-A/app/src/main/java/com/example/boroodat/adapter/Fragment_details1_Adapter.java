package com.example.boroodat.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.boroodat.databinding.FragmentDetails1ItemBinding;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.model.Activity6_Model;
import com.example.boroodat.model.Fragment_Details1_Model;

import java.util.List;

public class Fragment_details1_Adapter extends RecyclerView.Adapter<Fragment_details1_Adapter.viewHolder>
{
    private List<Fragment_Details1_Model> models;

    public Fragment_details1_Adapter(List<Fragment_Details1_Model> models)
    {
        this.models = models;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private FragmentDetails1ItemBinding binding;
        public viewHolder(FragmentDetails1ItemBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        FragmentDetails1ItemBinding binding=FragmentDetails1ItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);

        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {

        holder.setIsRecyclable(false);
        final Fragment_Details1_Model model = models.get(position);
        holder.itemView.setTag(model);

        //-------------------------------------------------------------------------------------------------------

       holder.binding.unitPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder.binding.unitPrice));
       holder.binding.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder.binding.totalPrice));

        //-------------------------------------------------------------------------------------------------------

        holder.binding.description.setText(model.getDescription());
        holder.binding.number.setText(model.getNumber());
        holder.binding.unitPrice.setText(model.getUnitPrice());
        holder.binding.totalPrice.setText(model.getTotalPrice());
        holder.binding.row.setText(model.getRow());

    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

}
