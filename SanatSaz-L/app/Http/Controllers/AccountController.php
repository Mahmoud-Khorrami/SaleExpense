<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Deposit;
use App\Models\Expense;
use App\Models\Material;
use App\Models\Salary;
use App\Models\Sale;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class AccountController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        $account = Account::create(
            [
                'user_id'        => $user->id,
                'company_id'     => $request->company_id,
                'title'          => $request->title,
                'account_number' => $request->account_number,
                'balance'        => $request->balance,
            ]
        );

        if ($request->balance)
        {
            Deposit::create(
                [
                    'user_id'     => $user->id,
                    'company_id'  => $request->company_id,
                    'title'       => 'موجودی اولیه',
                    'amount'      => $request->balance,
                    'account_id'  => $account->id,
                    'date'        => $request->date,
                    'description' => $request->description
                ]
            );
        }

        return ["code"    => "200",
                "message" => [
                    "id" => $account->id]];
    }

    public function accountsQuery1(Request $request)
    {
        $accounts = Account::where('company_id', $request->company_id)
                           ->whereNull("archive")
                           ->get();

        if ($accounts)
        {
            foreach ($accounts as $account)
            {
                $account->balance = $this->balance($request->company_id , $account->id);
                $account->save();
            }

            return ["code"    => "200",
                    "message" => [
                        "result" => $accounts,
                    ]];
        }

        return ["code"    => "207",
                'message' => trans('message1.207')];
    }

    public function archive(Request $request){

        $account=Account::where('id',$request->account_id)->first();

        if ($account)
        {
            $account->archive = "done";
            $account->save();
        }

        return ['code'=>'200'];
    }

    public function edit(Request $request)
    {
        $user = auth()->user();

        $account=Account::where('id',$request->account_id)->first();

        if ($account)
        {
            $balance = $account->balance;

            $initial_balance=0;

            $deposit = Deposit::where('account_id', $request->account_id)
                              ->where('title', 'موجودی اولیه')
                              ->first();

            if ($deposit)
                $initial_balance = $deposit->amount;

            $account->title = $request->title;
            $account->account_number = $request->account_number;
            $account->balance = $balance - $initial_balance + $request->balance;
            $account->save();

            if ($deposit)
            {
                if ($request->balance>0)
                {
                    $deposit->amount = $request->balance;
                    $deposit->save();
                }
                else
                    $deposit->delete();
            }

            else if ($request->balance>0)
            {
                Deposit::create(
                    [
                        'user_id'     => $user->id,
                        'company_id'  => $request->company_id,
                        'title'       => 'موجودی اولیه',
                        'amount'      => $request->balance,
                        'account_id'  => $account->id,
                        'date'        => $request->date,
                        'description' => "-"
                    ]
                );
            }

            return ['code'    => '200',
                    'balance' => $account->balance,];
        }

        return ['code' => '202', 'message' => trans('message1.202')];

    }

    public function delete(Request $request){

        $user = auth()->user();

        if (!Hash::check($request->password, $user->password))
            return ['code' => '205', 'message' => trans('message1.205')];

        else
        {
            $account=Account::where('id',$request->account_id)->first();

            if ($account)
                $account->delete();

            return ['code'=>'200'];
        }

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
