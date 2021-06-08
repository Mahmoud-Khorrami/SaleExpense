package com.example.boroodat.general;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.boroodat.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class WriteToExcel
{
    private Context context;
    private AlertDialog progressDialog;

    public WriteToExcel(Context context)
    {
        this.context = context;

        //----------------------------------------------------------------

        progressDialog = new SpotsDialog(context, R.style.Custom);
        progressDialog.setCancelable(true);
    }

    public  void  export(JSONObject response)
    {
        progressDialog.show();
        try
        {
            String path = Environment.getExternalStorageDirectory() + "/"+context.getString(R.string.app_name) + "/";
            File file1 = new File(path);

            if (!file1.isDirectory())
                file1.mkdirs();

            String file_name = "report-" + new TodayDate().dateTime() + ".xls";
            File file = new File( path , file_name );

            if (!file.exists ())
                file.createNewFile ();

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);

            //---------------------------------------------------------------------------

            WritableSheet sheet1 = workbook.createSheet("نام شرکت", 0);
            WritableSheet sheet2 = workbook.createSheet("کاربرها",1);
            WritableSheet sheet3 = workbook.createSheet("شماره حساب ها", 2);
            WritableSheet sheet4 = workbook.createSheet("خریدارها", 3);
            WritableSheet sheet5 = workbook.createSheet("راننده ها", 4);
            WritableSheet sheet6 = workbook.createSheet("پرسنل", 5);
            WritableSheet sheet7 = workbook.createSheet("فاکتور فروش", 6);
            WritableSheet sheet8 = workbook.createSheet("جزئیات فاکتور فروش", 7);
            WritableSheet sheet9 = workbook.createSheet("سایر دریافتی ها", 8);
            WritableSheet sheet10 = workbook.createSheet("حقوق ها", 9);
            WritableSheet sheet11 = workbook.createSheet("فاکتور هزینه", 10);
            WritableSheet sheet12 = workbook.createSheet("جزئیات فاکتور هزینه", 11);
            WritableSheet sheet13 = workbook.createSheet("فاکتور مواد اولیه", 12);
            WritableSheet sheet14 = workbook.createSheet("جزئیات فاکتور مواد اولیه", 13);

            //---------------------------------------------------------------------------

            sheet1.addCell(new Label(0, 0, "ردیف"));
            sheet1.addCell(new Label(1, 0, "نام شرکت"));
            sheet1.addCell(new Label(0, 1, new User_Info().company_id()));
            sheet1.addCell(new Label(1, 1, new User_Info().company_name()));

            //---------------------------------------------------------------------------

            try
            {
                boolean b1 = saveUsers(response.getJSONArray("users"),sheet2);
                boolean b2 = saveAccount(response.getJSONArray("accounts"),sheet3);
                boolean b3 = saveBuyer(response.getJSONArray("buyers"),sheet4);
                boolean b4 = saveDriver(response.getJSONArray("drivers"),sheet5);
                boolean b5 = savePersonnel(response.getJSONArray("personnel"),sheet6);
                boolean b6 = saveSale(response.getJSONArray("sales"),sheet7);
                boolean b7 = saveSaleDetail(response.getJSONArray("sale_details"),sheet8);
                boolean b8 = saveDeposit(response.getJSONArray("deposits"),sheet9);
                boolean b9 = saveSalary(response.getJSONArray("salaries"),sheet10);
                boolean b10 = saveExpense(response.getJSONArray("expenses"),sheet11);
                boolean b11 = saveExpenseDetail(response.getJSONArray("expense_details"),sheet12);
                boolean b12 = saveMaterial(response.getJSONArray("materials"),sheet13);
                boolean b13 = saveMaterialDetail(response.getJSONArray("material_details"),sheet14);

                if (b1 & b2 & b3 & b4 & b5 & b6 & b7 & b8 & b9 & b10 & b11 & b12 & b13 )
                {
                    progressDialog.dismiss();
                    workbook.write();
                    workbook.close();
                    Toast.makeText(context, "خروجی اکسل با موفقیت ایجاد شد.", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                progressDialog.dismiss();
                Toast.makeText(context, "ناموفق1", Toast.LENGTH_SHORT).show();
            }

            //---------------------------------------------------------------------------

        } catch (Exception e)
        {
            progressDialog.dismiss();
            Toast.makeText(context, "ناموفق2", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean saveUsers(JSONArray array , WritableSheet sheet2)
    {
        try
        {
            sheet2.addCell(new Label(0,0,"شناسه"));
            sheet2.addCell(new Label(1,0,"نام و نام خانوادگی"));
            sheet2.addCell(new Label(2,0,"شماره همراه"));
            sheet2.addCell(new Label(3,0,"سمت"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet2.addCell(new Label(0,i+1,object.getString("id")));
                sheet2.addCell(new Label(1,i+1,object.getString("name")));
                sheet2.addCell(new Label(2,i+1,object.getString("phone_number")));
                sheet2.addCell(new Label(3,i+1,object.getString("role")));

            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveAccount(JSONArray array , WritableSheet sheet3)
    {
        try
        {
            sheet3.addCell(new Label(0,0,"شناسه"));
            sheet3.addCell(new Label(1,0,"عنوان"));
            sheet3.addCell(new Label(2,0,"شماره حساب"));
            sheet3.addCell(new Label(3,0,"موجودی حساب"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet3.addCell(new Label(0,i+1,object.getString("id")));
                sheet3.addCell(new Label(1,i+1,object.getString("title")));
                sheet3.addCell(new Label(2,i+1,object.getString("account_number")));
                sheet3.addCell(new Label(3,i+1,object.getString("balance")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveBuyer(JSONArray array , WritableSheet sheet4)
    {
        try
        {
            sheet4.addCell(new Label(0,0,"شناسه"));
            sheet4.addCell(new Label(1,0,"نام و نام خانوادگی"));
            sheet4.addCell(new Label(2,0,"شماره همراه"));
            sheet4.addCell(new Label(3,0,"مقصد"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet4.addCell(new Label(0,i+1,object.getString("id")));
                sheet4.addCell(new Label(1,i+1,object.getString("name")));
                sheet4.addCell(new Label(2,i+1,object.getString("phone_number")));
                sheet4.addCell(new Label(3,i+1,object.getString("destination")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveDriver(JSONArray array , WritableSheet sheet5)
    {
        try
        {
            sheet5.addCell(new Label(0,0,"شناسه"));
            sheet5.addCell(new Label(1,0,"نام و نام خانوادگی"));
            sheet5.addCell(new Label(2,0,"شماره همراه"));
            sheet5.addCell(new Label(3,0,"نوع خودرو"));
            sheet5.addCell(new Label(4,0,"شماره پلاک خودرو"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet5.addCell(new Label(0,i+1,object.getString("id")));
                sheet5.addCell(new Label(1,i+1,object.getString("name")));
                sheet5.addCell(new Label(2,i+1,object.getString("phone_number")));
                sheet5.addCell(new Label(3,i+1,object.getString("car_type")));
                sheet5.addCell(new Label(4,i+1,object.getString("number_plate")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean savePersonnel(JSONArray array , WritableSheet sheet6)
    {
        try
        {
            sheet6.addCell(new Label(0,0,"شناسه"));
            sheet6.addCell(new Label(1,0,"نام و نام خانوادگی"));
            sheet6.addCell(new Label(2,0,"شماره همراه"));
            sheet6.addCell(new Label(3,0,"تاریخ عضویت"));
            sheet6.addCell(new Label(4,0,"سمت"));
            sheet6.addCell(new Label(5,0,"شماره کارت"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet6.addCell(new Label(0,i+1,object.getString("id")));
                sheet6.addCell(new Label(1,i+1,object.getString("name")));
                sheet6.addCell(new Label(2,i+1,object.getString("phone_number")));
                sheet6.addCell(new Label(3,i+1,object.getString("register_date")));
                sheet6.addCell(new Label(4,i+1,object.getString("role")));
                sheet6.addCell(new Label(5,i+1,object.getString("credit_card")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveSale(JSONArray array , WritableSheet sheet7)
    {
        try
        {
            sheet7.addCell(new Label(0,0,"شناسه"));
            sheet7.addCell(new Label(1,0,"شماره فاکتور"));
            sheet7.addCell(new Label(2,0,"تاریخ"));
            sheet7.addCell(new Label(3,0,"شناسه خریدار"));
            sheet7.addCell(new Label(4,0,"نام خریدار"));
            sheet7.addCell(new Label(5,0,"شناسه راننده"));
            sheet7.addCell(new Label(6,0,"نام راننده"));
            sheet7.addCell(new Label(7,0,"جمع مبلغ فاکتور"));
            sheet7.addCell(new Label(8,0,"مبلغ دریافت شده"));
            sheet7.addCell(new Label(9,0,"مبلغ باقیمانده"));
            sheet7.addCell(new Label(10,0,"شناسه حساب بانکی"));
            sheet7.addCell(new Label(11,0,"عنوان حساب بانکی"));
            sheet7.addCell(new Label(12,0,"شرح"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("sale");

                sheet7.addCell(new Label(0,i+1,object1.getString("id")));
                sheet7.addCell(new Label(1,i+1,object1.getString("factor_number")));
                sheet7.addCell(new Label(2,i+1,object1.getString("date")));
                sheet7.addCell(new Label(3,i+1,object1.getString("buyer_id")));
                sheet7.addCell(new Label(4,i+1,object.getString("buyer_name")));
                sheet7.addCell(new Label(5,i+1,object1.getString("driver_id")));
                sheet7.addCell(new Label(6,i+1,object.getString("driver_name")));
                sheet7.addCell(new Label(7,i+1,object1.getString("sum")));
                sheet7.addCell(new Label(8,i+1,object1.getString("payment")));
                sheet7.addCell(new Label(9,i+1,object1.getString("remain")));
                sheet7.addCell(new Label(10,i+1,object1.getString("account_id")));
                sheet7.addCell(new Label(11,i+1,object.getString("account_title")));
                sheet7.addCell(new Label(12,i+1,object1.getString("description")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveSaleDetail(JSONArray array , WritableSheet sheet8)
    {
        try
        {
            sheet8.addCell(new Label(0,0,"شناسه"));
            sheet8.addCell(new Label(1,0,"شناسه فاکتور فروش"));
            sheet8.addCell(new Label(2,0,"ردیف"));
            sheet8.addCell(new Label(3,0,"شرح"));
            sheet8.addCell(new Label(4,0,"تعداد"));
            sheet8.addCell(new Label(5,0,"قیمت واحد"));
            sheet8.addCell(new Label(6,0,"قیمت کل"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet8.addCell(new Label(0,i+1,object.getString("id")));
                sheet8.addCell(new Label(1,i+1,object.getString("sale_id")));
                sheet8.addCell(new Label(2,i+1,object.getString("row")));
                sheet8.addCell(new Label(3,i+1,object.getString("description")));
                sheet8.addCell(new Label(4,i+1,object.getString("number")));
                sheet8.addCell(new Label(5,i+1,object.getString("unit_price")));
                sheet8.addCell(new Label(6,i+1,object.getString("total_price")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveDeposit(JSONArray array , WritableSheet sheet9)
    {
        try
        {
            sheet9.addCell(new Label(0,0,"شناسه"));
            sheet9.addCell(new Label(1,0,"عنوان"));
            sheet9.addCell(new Label(2,0,"مبلغ"));
            sheet9.addCell(new Label(3,0,"شناسه حساب بانکی"));
            sheet9.addCell(new Label(4,0,"عنوان حساب بانکی"));
            sheet9.addCell(new Label(5,0,"تاریخ"));
            sheet9.addCell(new Label(6,0,"توضیحات"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("deposit");

                sheet9.addCell(new Label(0,i+1,object1.getString("id")));
                sheet9.addCell(new Label(1,i+1,object1.getString("title")));
                sheet9.addCell(new Label(2,i+1,object1.getString("amount")));
                sheet9.addCell(new Label(3,i+1,object1.getString("account_id")));
                sheet9.addCell(new Label(4,i+1,object.getString("account_title")));
                sheet9.addCell(new Label(5,i+1,object1.getString("date")));
                sheet9.addCell(new Label(6,i+1,object1.getString("description")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveSalary(JSONArray array , WritableSheet sheet10)
    {
        try
        {
            sheet10.addCell(new Label(0,0,"شناسه"));
            sheet10.addCell(new Label(1,0,"شناسه پرسنل"));
            sheet10.addCell(new Label(2,0,"نام پرسنل"));
            sheet10.addCell(new Label(3,0,"حقوق"));
            sheet10.addCell(new Label(4,0,"بیعانه"));
            sheet10.addCell(new Label(5,0,"بیمه و مالیات"));
            sheet10.addCell(new Label(6,0,"شناسه حساب بانکی"));
            sheet10.addCell(new Label(7,0,"عنوان حساب بانکی"));
            sheet10.addCell(new Label(8,0,"تاریخ"));
            sheet10.addCell(new Label(9,0,"توضیحات"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("salary");

                sheet10.addCell(new Label(0,i+1,object1.getString("id")));
                sheet10.addCell(new Label(1,i+1,object1.getString("personnel_id")));
                sheet10.addCell(new Label(2,i+1,object.getString("personnel_name")));
                sheet10.addCell(new Label(3,i+1,object1.getString("salary")));
                sheet10.addCell(new Label(4,i+1,object1.getString("earnest")));
                sheet10.addCell(new Label(5,i+1,object1.getString("insurance_tax")));
                sheet10.addCell(new Label(6,i+1,object1.getString("account_id")));
                sheet10.addCell(new Label(7,i+1,object.getString("account_title")));
                sheet10.addCell(new Label(8,i+1,object1.getString("date")));
                sheet10.addCell(new Label(9,i+1,object1.getString("description")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveExpense(JSONArray array , WritableSheet sheet11)
    {
        try
        {
            sheet11.addCell(new Label(0,0,"شناسه"));
            sheet11.addCell(new Label(1,0,"شماره فاکتور"));
            sheet11.addCell(new Label(2,0,"تاریخ"));
            sheet11.addCell(new Label(3,0,"جمع مبلغ فاکتور"));
            sheet11.addCell(new Label(4,0,"پرداخت شده"));
            sheet11.addCell(new Label(5,0,"باقیمانده"));
            sheet11.addCell(new Label(6,0,"شناسه حساب بانکی"));
            sheet11.addCell(new Label(7,0,"عنوان حساب بانکی"));
            sheet11.addCell(new Label(8,0,"شرح"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("expense");

                sheet11.addCell(new Label(0,i+1,object1.getString("id")));
                sheet11.addCell(new Label(1,i+1,object1.getString("factor_number")));
                sheet11.addCell(new Label(2,i+1,object1.getString("date")));
                sheet11.addCell(new Label(3,i+1,object1.getString("sum")));
                sheet11.addCell(new Label(4,i+1,object1.getString("payment")));
                sheet11.addCell(new Label(5,i+1,object1.getString("remain")));
                sheet11.addCell(new Label(6,i+1,object1.getString("account_id")));
                sheet11.addCell(new Label(7,i+1,object.getString("account_title")));
                sheet11.addCell(new Label(8,i+1,object1.getString("description")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveExpenseDetail(JSONArray array , WritableSheet sheet12)
    {
        try
        {
            sheet12.addCell(new Label(0,0,"شناسه"));
            sheet12.addCell(new Label(1,0,"شناسه فاکتور هزینه"));
            sheet12.addCell(new Label(2,0,"ردیف"));
            sheet12.addCell(new Label(3,0,"شرح"));
            sheet12.addCell(new Label(4,0,"تعداد"));
            sheet12.addCell(new Label(5,0,"قیمت واحد"));
            sheet12.addCell(new Label(6,0,"قیمت کل"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet12.addCell(new Label(0,i+1,object.getString("id")));
                sheet12.addCell(new Label(1,i+1,object.getString("expense_id")));
                sheet12.addCell(new Label(2,i+1,object.getString("row")));
                sheet12.addCell(new Label(3,i+1,object.getString("description")));
                sheet12.addCell(new Label(4,i+1,object.getString("number")));
                sheet12.addCell(new Label(5,i+1,object.getString("unit_price")));
                sheet12.addCell(new Label(6,i+1,object.getString("total_price")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveMaterial(JSONArray array , WritableSheet sheet13)
    {
        try
        {
            sheet13.addCell(new Label(0,0,"شناسه"));
            sheet13.addCell(new Label(1,0,"شماره فاکتور"));
            sheet13.addCell(new Label(2,0,"تاریخ"));
            sheet13.addCell(new Label(3,0,"جمع مبلغ فاکتور"));
            sheet13.addCell(new Label(4,0,"پرداخت شده"));
            sheet13.addCell(new Label(5,0,"باقیمانده"));
            sheet13.addCell(new Label(6,0,"شناسه حساب بانکی"));
            sheet13.addCell(new Label(7,0,"عنوان حساب بانکی"));
            sheet13.addCell(new Label(8,0,"شرح"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);
                JSONObject object1 = object.getJSONObject("material");

                sheet13.addCell(new Label(0,i+1,object1.getString("id")));
                sheet13.addCell(new Label(1,i+1,object1.getString("factor_number")));
                sheet13.addCell(new Label(2,i+1,object1.getString("date")));
                sheet13.addCell(new Label(3,i+1,object1.getString("sum")));
                sheet13.addCell(new Label(4,i+1,object1.getString("payment")));
                sheet13.addCell(new Label(5,i+1,object1.getString("remain")));
                sheet13.addCell(new Label(6,i+1,object1.getString("account_id")));
                sheet13.addCell(new Label(7,i+1,object.getString("account_title")));
                sheet13.addCell(new Label(8,i+1,object1.getString("description")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private boolean saveMaterialDetail(JSONArray array , WritableSheet sheet14)
    {
        try
        {
            sheet14.addCell(new Label(0,0,"شناسه"));
            sheet14.addCell(new Label(1,0,"شناسه فاکتور خرید مواد اولیه"));
            sheet14.addCell(new Label(2,0,"ردیف"));
            sheet14.addCell(new Label(3,0,"شرح"));
            sheet14.addCell(new Label(4,0,"تعداد"));
            sheet14.addCell(new Label(5,0,"قیمت واحد"));
            sheet14.addCell(new Label(6,0,"قیمت کل"));

            for (int i=0; i<array.length(); i++)
            {
                JSONObject object = array.getJSONObject(i);

                sheet14.addCell(new Label(0,i+1,object.getString("id")));
                sheet14.addCell(new Label(1,i+1,object.getString("material_id")));
                sheet14.addCell(new Label(2,i+1,object.getString("row")));
                sheet14.addCell(new Label(3,i+1,object.getString("description")));
                sheet14.addCell(new Label(4,i+1,object.getString("number")));
                sheet14.addCell(new Label(5,i+1,object.getString("unit_price")));
                sheet14.addCell(new Label(6,i+1,object.getString("total_price")));
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

}
