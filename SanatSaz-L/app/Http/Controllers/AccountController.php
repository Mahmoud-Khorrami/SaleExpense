<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Deposit;
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

        return ['id' => $account->id];
    }

    public function edit(Request $request)
    {
        $account=Account::where('id',$request->account_id)->first();

        if ($account)
        {
            $balance = $account->balance;

            $deposit = Deposit::where('account_id', $request->account_id)->where('title', 'موجودی اولیه')->first();
            $initial_balance = $deposit->amount;

            $account->title = $request->title;
            $account->account_number = $request->account_number;
            $account->balance = $balance - $initial_balance + $request->balance;
            $account->save();

            $deposit->amount = $request->balance;
            $deposit->save();


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
}
