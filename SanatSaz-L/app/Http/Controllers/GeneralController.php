<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Buyer;
use App\Models\Company;
use App\Models\Deposit;
use App\Models\Driver;
use App\Models\Expense;
use App\Models\ExpenseDetail;
use App\Models\Material;
use App\Models\MaterialDetail;
use App\Models\Personnel;
use App\Models\Salary;
use App\Models\Sale;
use App\Models\SaleDetail;
use App\Models\User;
use Illuminate\Http\Request;

class GeneralController extends Controller
{

    public function data1(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //--------------------------------------------------------------------------

        $drivers = Driver::where('company_id', $request->company_id)->get();
        $buyers = Buyer::where('company_id', $request->company_id)->get();
        $personnel = Personnel::where('company_id', $request->company_id)->get();

        //--------------------------------------------------------------------------

        return ['drivers'   => $drivers,
                'buyers'    => $buyers,
                'accounts'  => $accounts,
                'personnel' => $personnel,
                'reports'   => $this->report($request)];
    }

    public function data2(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id, $account->id);
            $account->save();
        }

        //-----------------------------------------------------------------------

        $sales = Sale::where('company_id', $request->company_id)->get();
        $deposits = Deposit::where('company_id', $request->company_id)->get();

        //-----------------------------------------------------------------------
        $details = collect([]);

        foreach ($sales as $sale)
        {
            $saleDetails = $sale->saleDetails;

            $description = '';

            foreach ($saleDetails as $saleDetail)
            {
                $description = $description . $saleDetail->description . ' ' . $saleDetail->number . ' عدد، ';
            }

            $details->push(
                ["id"          => $sale->id,
                 "description" => $description]
            );
        }

        //-----------------------------------------------------------------------
        return ['sales'    => $sales,
                'deposits' => $deposits,
                'accounts' => $accounts,
                'details'  => $details];
    }

    public function data3(Request $request)
    {

        $personnel = Personnel::where('company_id', $request->company_id)->get();
        $sales = Sale::where('company_id', $request->company_id)->where('account_id', $request->account_id)->get();
        $deposits = Deposit::where('company_id', $request->company_id)->where('account_id', $request->account_id)->get();
        $salaries = Salary::where('company_id', $request->company_id)->where('account_id', $request->account_id)->get();
        $expenses = Expense::where('company_id', $request->company_id)->where('account_id', $request->account_id)->get();
        $materials = Material::where('company_id', $request->company_id)->where('account_id', $request->account_id)->get();

        //-----------------------------------------------------------------------
        $details = collect([]);

        foreach ($sales as $sale)
        {
            $saleDetails = $sale->saleDetails;

            $description = '';

            foreach ($saleDetails as $saleDetail)
            {
                $description = $description . $saleDetail->description . ' ' . $saleDetail->number . ' عدد، ';
            }

            $details->push(
                ["id"          => $sale->id,
                 "description" => $description]
            );
        }

        //-----------------------------------------------------------------------

        return ['personnel' => $personnel,
                'sales'     => $sales,
                'deposits'  => $deposits,
                'salaries'  => $salaries,
                'expenses'  => $expenses,
                'materials' => $materials,
                'details'   => $details];
    }

    public function data4(Request $request)
    {

        $sales = Sale::where('company_id', $request->company_id)->where('buyer_id', $request->buyer_id)->get();

        //-----------------------------------------------------------------------
        $details = collect([]);

        foreach ($sales as $sale)
        {
            $saleDetails = $sale->saleDetails;

            $description = '';

            foreach ($saleDetails as $saleDetail)
            {
                $description = $description . $saleDetail->description . ' ' . $saleDetail->number . ' عدد، ';
            }

            $details->push(
                ["id"          => $sale->id,
                 "description" => $description]
            );
        }

        //-----------------------------------------------------------------------

        return ['sales'   => $sales,
                'details' => $details];
    }

    public function data5(Request $request)
    {

        $sales = Sale::where('company_id', $request->company_id)->where('driver_id', $request->driver_id)->get();

        //-----------------------------------------------------------------------
        $details = collect([]);

        foreach ($sales as $sale)
        {
            $saleDetails = $sale->saleDetails;

            $description = '';

            foreach ($saleDetails as $saleDetail)
            {
                $description = $description . $saleDetail->description . ' ' . $saleDetail->number . ' عدد، ';
            }

            $details->push(
                ["id"          => $sale->id,
                 "description" => $description]
            );
        }

        //-----------------------------------------------------------------------
        return ['sales'   => $sales,
                'details' => $details];
    }

    public function data6(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $personnel = Personnel::where('company_id', $request->company_id)->get();
        $salaries = Salary::where('company_id', $request->company_id)->get();
        $expenses = Expense::where('company_id', $request->company_id)->get();
        $materials = Material::where('company_id', $request->company_id)->get();

        return ['accounts'  => $accounts,
                'personnel' => $personnel,
                'salaries'  => $salaries,
                'expenses'  => $expenses,
                'materials' => $materials];
    }

    public function data7(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $drivers = Driver::where('company_id', $request->company_id)->get();
        $buyers = Buyer::where('company_id', $request->company_id)->get();

        return ['drivers'   => $drivers,
                'buyers'    => $buyers,
                'accounts'  => $accounts,];
    }

    public function data8(Request $request)
    {
        $user = auth()->user();

        //---------------------------------------------------------------------

        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $expenses = Expense::where('company_id', $request->company_id)->where('user_id', $user->id)->get();
        $sales = Sale::where('company_id', $request->company_id)->where('user_id', $user->id)->get();

        //-----------------------------------------------------------------------
        $details = collect([]);

        foreach ($sales as $sale)
        {
            $saleDetails = $sale->saleDetails;

            $description = '';

            foreach ($saleDetails as $saleDetail)
            {
                $description = $description . $saleDetail->description . ' ' . $saleDetail->number . ' عدد، ';
            }

            $details->push(
                ["id"          => $sale->id,
                 "description" => $description]
            );
        }

        //-----------------------------------------------------------------------
        return [
            'expenses' => $expenses,
            'sales'    => $sales,
            'accounts' => $accounts,
            'details'=>$details];
    }

    public function data9(Request $request)
    {
        $salaries = Salary::where([['company_id', $request->company_id], ['personnel_id', $request->personnel_id]])->get();

        return ['salaries' => $salaries,];
    }

    public function data10(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        return ['accounts'  => $accounts,
                'reports'   => $this->report($request)];
    }

    public function data11(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $personnel = Personnel::where('company_id', $request->company_id)->get();

        return ['accounts'  => $accounts,
                'personnel'=>$personnel,
                'reports'   => $this->report($request)];
    }

    public function data12(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $buyers = Buyer::where('company_id', $request->company_id)->get();

        return ['accounts'  => $accounts,
                'buyers'=>$buyers,
                'reports'   => $this->report($request)];
    }

    public function data13(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id , $account->id);
            $account->save();
        }

        //----------------------------------------------------------------------

        $drivers = Driver::where('company_id', $request->company_id)->get();

        return ['accounts'  => $accounts,
                'drivers'=>$drivers,
                'reports'   => $this->report($request)];
    }

    public function data14(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)->get();

        foreach ($accounts as $account)
        {
            $account->balance = $this->balance($request->company_id, $account->id);
            $account->save();
        }

        //---------------------------------------------------------------

        $users = User::where('company_id', $request->company_id)->get();
        $buyers = Buyer::where('company_id', $request->company_id)->get();
        $drivers = Driver::where('company_id', $request->company_id)->get();
        $personnel = Personnel::where('company_id', $request->company_id)->get();
        $sales = Sale::where('company_id', $request->company_id)->get();
        $sale_details = SaleDetail::where('company_id', $request->company_id)->get();
        $deposits = Deposit::where('company_id', $request->company_id)->get();
        $salaries = Salary::where('company_id', $request->company_id)->get();
        $expenses = Expense::where('company_id', $request->company_id)->get();
        $expense_details = ExpenseDetail::where('company_id', $request->company_id)->get();
        $materials = Material::where('company_id', $request->company_id)->get();
        $material_details = MaterialDetail::where('company_id', $request->company_id)->get();

        return [
            'users'            => $users,
            'accounts'         => $accounts,
            'buyers'           => $buyers,
            'drivers'          => $drivers,
            'personnel'        => $personnel,
            'sales'            => $sales,
            'sale_details'     => $sale_details,
            'deposits'         => $deposits,
            'salaries'         => $salaries,
            'expenses'         => $expenses,
            'expense_details'  => $expense_details,
            'materials'        => $materials,
            'material_details' => $material_details];

    }

    public function report(Request $request){

        $materials = Material::where('company_id', $request->company_id)->get();

        $material = 0;
        $material_payment = 0;

        foreach ($materials as $item)
        {
            $material = $material + $item->sum;
            $material_payment = $material_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $salaries = Salary::where('company_id', $request->company_id)->get();

        $salary = 0;

        foreach ($salaries as $item)
        {
            $salary = $salary + $item->salary + $item->earnest + $item->insurance_tax;
        }

        //--------------------------------------------------------------------------

        $expenses = Expense::where('company_id', $request->company_id)->get();

        $expense = 0;
        $expense_payment = 0;

        foreach ($expenses as $item)
        {
            $expense = $expense + $item->sum;
            $expense_payment = $expense_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $sales = Sale::where('company_id', $request->company_id)->get();

        $sale = 0;
        $sale_payment = 0;

        foreach ($sales as $item)
        {
            $sale = $sale + $item->sum;
            $sale_payment = $sale_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $deposits = Deposit::where('company_id', $request->company_id)->get();

        $deposit = 0;

        foreach ($deposits as $item)
        {
            $deposit = $deposit + $item->amount;
        }

        //--------------------------------------------------------------------------
        $reports = collect([]);
        $reports->push(['material'  => $material,
                        'material_payment'  => $material_payment,
                        'salary'  => $salary,
                        'expense' => $expense,
                        'expense_payment'  => $expense_payment,
                        'sale'  => $sale,
                        'sale_payment'  => $sale_payment,
                        'deposit' => $deposit,]);

        //--------------------------------------------------------------------------

        return $reports;
    }

    public function balance($company_id , $account_id){

        $materials = Material::where('company_id',$company_id)->where('account_id' ,$account_id)->get();

        $material_payment = 0;

        foreach ($materials as $item)
        {
            $material_payment = $material_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $salaries = Salary::where('company_id',$company_id)->where('account_id' ,$account_id)->get();

        $salary = 0;

        foreach ($salaries as $item)
        {
            $salary = $salary + $item->salary + $item->earnest + $item->insurance_tax;
        }

        //--------------------------------------------------------------------------

        $expenses = Expense::where('company_id',$company_id)->where('account_id' ,$account_id)->get();

        $expense_payment = 0;

        foreach ($expenses as $item)
        {
            $expense_payment = $expense_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $sales = Sale::where('company_id',$company_id)->where('account_id' ,$account_id)->get();

        $sale_payment = 0;

        foreach ($sales as $item)
        {
            $sale_payment = $sale_payment + $item->payment;
        }

        //--------------------------------------------------------------------------

        $deposits = Deposit::where('company_id',$company_id)->where('account_id' ,$account_id)->get();

        $deposit = 0;

        foreach ($deposits as $item)
        {
            $deposit = $deposit + $item->amount;
        }

        //--------------------------------------------------------------------------

        return $sale_payment + $deposit - $material_payment - $expense_payment -$salary;
    }

}

    /*public function getData(Request $request)
    {
        $sales = Sale::where('company_id', $request->company_id)->get();

        $buyer_name = collect([]);
        $destination = collect([]);
        $basket_type = collect([]);
        $driver_name = collect([]);
        $driver_phone = collect([]);
        $car_type = collect([]);
        $number_plate = collect([]);

        foreach ($sales as $sale)
        {
            $buyer_name->push($sale->buyer_name);
            $destination->push($sale->destination);
            $basket_type->push($sale->basket_type);
            $driver_name->push($sale->driver_name);
            $driver_phone->push($sale->driver_phone);
            $car_type->push($sale->car_type);
            $number_plate->push($sale->number_plate);
        }

        $buyer_name1 = $buyer_name->unique();
        $destination1 = $destination->unique();
        $basket_type1 = $basket_type->unique();
        $driver_name1 = $driver_name->unique();
        $driver_phone1 = $driver_phone->unique();
        $car_type1 = $car_type->unique();
        $number_plate1 = $number_plate->unique();

        //-------------------------------------------------------------------------------

        $users = User::where('company_id', $request->company_id)->get();

        //-------------------------------------------------------------------------------

        return [
            'buyer_name'   => $buyer_name1->values()->all(),
            'destination'  => $destination1->values()->all(),
            'basket_type'  => $basket_type1->values()->all(),
            'driver_name'  => $driver_name1->values()->all(),
            'driver_phone' => $driver_phone1->values()->all(),
            'car_type'     => $car_type1->values()->all(),
            'number_plate' => $number_plate1->values()->all(),
            'users'        => $users,
        ];
    }
}*/
