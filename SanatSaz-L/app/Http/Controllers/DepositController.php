<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Company;
use App\Models\Deposit;
use Illuminate\Http\Request;

class DepositController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        Deposit::create(
            [
                'user_id'     => $user->id,
                'company_id'  => $request->company_id,
                'title'       => $request->title,
                'amount'      => $request->amount,
                'account_id'  => $request->account_id,
                'date'        => $request->date,
                'description' => $request->description
            ]
        );

        return ['code' => '200'];
    }

    public function initialBalance(Request $request){

        $initial_balance=Deposit::where('account_id',$request->account_id)->where('title','موجودی اولیه')->first();

        return ['initial_balance'=>$initial_balance->amount];
    }

    public function getDeposit(Request $request)
    {

        $deposit = Deposit::where('id', $request->deposit_id)->first();
        if ($deposit)
        {
            return ['code'          => '200',
                    'deposit'       => $deposit,
                    'account_title' => $deposit->account->title];
        }

        else
            return [
                'code'    => '202',
                'message' => trans('message1.202')
            ];
    }

    public function edit(Request $request)
    {
        $user = auth()->user();

        $deposit = Deposit::where('id', $request->deposit_id)->first();

        if ($deposit)
        {
            $deposit->user_id = $user->id;
            $deposit->title=$request->title;
            $deposit->amount = $request->amount;
            $deposit->account_id=$request->account_id;
            $deposit->date = $request->date;
            $deposit->description = $request->description;

            $deposit->save();
        }

        else
            $this->create($request);

        return ['code'=>'200'];
    }

    public function delete(Request $request)
    {
        $deposit=Deposit::where('id',$request->deposit_id)->first();

        if ($deposit)
            $deposit->delete();

        return ['code'=>'200'];
    }
}
