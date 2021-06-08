package com.example.boroodat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.boroodat.R;
import com.example.boroodat.database.User_Info_DB;
import com.example.boroodat.databinding.Activity20UserBinding;
import com.example.boroodat.databinding.Dialog1Binding;
import com.example.boroodat.general.Internet;
import com.example.boroodat.general.NavigationIconClickListener;
import com.example.boroodat.general.User_Info;
import com.google.android.material.card.MaterialCardView;

import dmax.dialog.SpotsDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class Activity20_User extends AppCompatActivity
{

    private Activity20UserBinding binding;
    private AlertDialog progressDialog;
    private Context context=this;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = Activity20UserBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        realm = Realm.getDefaultInstance();
        //-------------------------------------------------------------------------------------------------------

        binding.appBar.setTitle("");
        binding.title.setText(new User_Info().company_name());
        setSupportActionBar(binding.appBar);

        //-------------------------------------------------------------------------------------------------------

        progressDialog = new SpotsDialog(this,R.style.Custom);
        progressDialog.setCancelable(true);

        //-------------------------------------------------------------------------------------------------------

        binding.sale.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity6_RecordSales.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });
        //-------------------------------------------------------------------------------------------------------

        binding.expense.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(Activity20_User.this, Activity16_RecordExpense.class);
                intent.putExtra("from","user");
                startActivity(intent);
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.saleReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                {
                    Intent intent = new Intent(Activity20_User.this,Activity5_SaleReports.class);
                    intent.putExtra("from","user");
                    startActivity(intent);
                }
                else
                    new Internet(context).enable();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.expenseReports.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (new Internet(context).check())
                {
                    Intent intent = new Intent(Activity20_User.this,Activity15_ExpenseReports.class);
                    intent.putExtra("from","user");
                    startActivity(intent);
                }
                else
                    new Internet(context).enable();
            }
        });

        //-------------------------------------------------------------------------------------------------------

        binding.deletePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                final User_Info_DB info = realm.where(User_Info_DB.class).findFirst();

                if (!info.getPassword().equals(""))
                {
                    final Dialog1Binding binding1 = Dialog1Binding.inflate(LayoutInflater.from(context));
                    View view1 = binding1.getRoot();
                    androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                    alertDialogBuilder.setView(view1);

                    //-------------------------------------------------------------------------------------------------------

                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("تایید", null);
                    alertDialogBuilder.setNeutralButton("لغو", null);
                    final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();

                    //-------------------------------------------------------------------------------------------------------

                    binding1.message.setText("آیا مایلید رمز عبور را از حافظه حذف کنید؟");

                    //-------------------------------------------------------------------------------------------------------

                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener()
                    {
                        @Override
                        public void onShow(DialogInterface dialogInterface)
                        {
                            Button approve = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
                            approve.setTextColor(context.getResources().getColor(R.color.black));
                            approve.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View view)
                                {

                                    realm.executeTransaction(new Realm.Transaction()
                                    {
                                        @Override
                                        public void execute(Realm realm)
                                        {
                                            User_Info_DB infoDb = realm.where(User_Info_DB.class).findFirst();
                                            infoDb.setPassword("");
                                            Toast.makeText(context,"حذف رمز عبور از حافظه با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                                            alertDialog.dismiss();
                                        }
                                    });
                                }
                            });

                            Button Cancel = alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL);
                            Cancel.setTextColor(context.getResources().getColor(R.color.black));
                            Cancel.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    alertDialog.dismiss();
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

                else
                    Toast.makeText(context,"رمز عبور ذخیره نشده است.",Toast.LENGTH_LONG).show();
            }
        });
    }
}
