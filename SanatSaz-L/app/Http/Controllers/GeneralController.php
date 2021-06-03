<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Buyer;
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
    public function xls(Request $request)
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

    public function report1(Request $request)
    {

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
        $reports->push(
            ['material'         => $material,
             'material_payment' => $material_payment,
             'salary'           => $salary,
             'expense'          => $expense,
             'expense_payment'  => $expense_payment,
             'sale'             => $sale,
             'sale_payment'     => $sale_payment,
             'deposit'          => $deposit,]
        );

        //--------------------------------------------------------------------------

        return ["code"   => "200",
                "result" => $reports];
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
