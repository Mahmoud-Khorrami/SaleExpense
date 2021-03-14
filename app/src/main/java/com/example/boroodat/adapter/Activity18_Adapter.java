package com.example.boroodat.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boroodat.database.Activity18_DB;
import com.example.boroodat.databinding.A18ItemBinding;
import com.example.boroodat.general.NumberTextWatcherForThousand;
import com.example.boroodat.model.Activity18_Model;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Activity18_Adapter extends RecyclerView.Adapter<Activity18_Adapter.viewHolder>
{
    private List<Activity18_Model> models;
    private Context context;
    private Realm realm;
    private TextInputEditText sum;

    public Activity18_Adapter(List<Activity18_Model> models, Context context, TextInputEditText sum)
    {
        this.models = models;
        this.context = context;
        this.sum=sum;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private A18ItemBinding binding;
        public viewHolder(A18ItemBinding binding)
        {
            super(binding.getRoot());
            this.binding=binding;

            realm=Realm.getDefaultInstance();
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        A18ItemBinding binding=A18ItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false);

        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {

        holder.setIsRecyclable(false);
        final Activity18_Model model = models.get(position);
        holder.itemView.setTag(model);

        //-------------------------------------------------------------------------------------------------------

        holder.binding.unitPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder.binding.unitPrice));
        holder.binding.totalPrice.addTextChangedListener(new NumberTextWatcherForThousand(holder.binding.totalPrice));

        //-------------------------------------------------------------------------------------------------------

        RealmResults<Activity18_DB> res = realm.where(Activity18_DB.class)
                .equalTo("id", model.getId())
                .findAll();


        holder.binding.description.setText(res.get(0).getDescription());
        holder.binding.number.setText(res.get(0).getNumber());
        holder.binding.unitPrice.setText(res.get(0).getUnitPrice());
        holder.binding.totalPrice.setText(res.get(0).getTotalPrice());
        holder.binding.row.setText(res.get(0).getRow());

        //-------------------------------------------------------------------------------------------------------

        holder.binding.number.addTextChangedListener(new Watcher(model.getId(),model.getRow(),holder.binding.description,holder.binding.number,holder.binding.unitPrice,holder.binding.totalPrice));
        holder.binding.unitPrice.addTextChangedListener(new Watcher(model.getId(),model.getRow(),holder.binding.description,holder.binding.number,holder.binding.unitPrice,holder.binding.totalPrice));
        holder.binding.description.addTextChangedListener(new Watcher(model.getId(),model.getRow(),holder.binding.description,holder.binding.number,holder.binding.unitPrice,holder.binding.totalPrice));
    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public class Watcher implements TextWatcher
    {
        private int id;
        private String row;
        private TextInputEditText description, number,unitPrice,totalPrice;

        public Watcher(int id,String row, TextInputEditText description, TextInputEditText number, TextInputEditText unitPrice, TextInputEditText totalPrice)
        {
            this.id = id;
            this.row = row;
            this.description = description;
            this.number = number;
            this.unitPrice = unitPrice;
            this.totalPrice = totalPrice;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            double number1=0;
            double unitPrice1=0;

            if (!number.getText().toString().equals(""))
                number1=Double.parseDouble(number.getText().toString());
            if (!unitPrice.getText().toString().equals(""))
                unitPrice1=Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(unitPrice.getText().toString()));

            double totalPrice1=number1*unitPrice1;

            totalPrice.setText(Math.round(totalPrice1)+"");

            //-------------------------------------------------------------------------------------------------------

            realm.beginTransaction ();
            realm.copyToRealmOrUpdate ( new Activity18_DB( id,row, description.getText().toString(), number.getText().toString()
                    ,NumberTextWatcherForThousand.trimCommaOfString(unitPrice.getText().toString())
                    ,NumberTextWatcherForThousand.trimCommaOfString(totalPrice.getText().toString())) );
            realm.commitTransaction ();

            //-------------------------------------------------------------------------------------------------------

            RealmResults<Activity18_DB> results = realm.where ( Activity18_DB.class ).findAll();

            double sum1=0;
            for (int i=0;i<results.size();i++)
            {
                if (!results.get(i).getTotalPrice().equals(""))
                    sum1=sum1+Double.parseDouble(NumberTextWatcherForThousand.trimCommaOfString(results.get(i).getTotalPrice()));
            }

            sum.setText(Math.round(sum1)+"");
        }
    }
}
