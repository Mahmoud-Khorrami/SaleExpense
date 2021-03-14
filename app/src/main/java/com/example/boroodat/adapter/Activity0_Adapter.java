package com.example.boroodat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boroodat.interfaces.RecyclerViewItemClickInterface1;
import com.example.boroodat.model.Activity0_Model;
import com.example.boroodat.R;

import java.util.List;

public class Activity0_Adapter extends RecyclerView.Adapter<Activity0_Adapter.viewHolder> implements View.OnClickListener
{
    private List<Activity0_Model> models;
    private Context context;
    private RecyclerViewItemClickInterface1 listener;

    public Activity0_Adapter(List<Activity0_Model> models, Context context)
    {
        this.models = models;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.a0_item, parent, false );

        view.setOnClickListener(Activity0_Adapter.this);
        return new viewHolder ( view );
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        holder.setIsRecyclable ( false );
        final Activity0_Model model = models.get ( position );

        holder.itemView.setTag ( model );

        holder.name.setText(model.getName());
        holder.date.setText(model.getDate());
        holder.account.setText(model.getAccount());

    }

    @Override
    public int getItemCount()
    {
        return models.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {

        TextView name,date,account;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            date=itemView.findViewById(R.id.date);
            account=itemView.findViewById(R.id.account);
        }
    }

    public void setOnItemClickListener(RecyclerViewItemClickInterface1 listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v)
    {
        if (listener != null)
        {
            Activity0_Model model = (Activity0_Model) v.getTag ();
            listener.onItemClick ( v, model );
        }
    }
}
