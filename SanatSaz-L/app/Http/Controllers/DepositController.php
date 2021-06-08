<?php

namespace App\Http\Controllers;

use App\Http\Resources\DepositResource1;
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

        if ($initial_balance)
            return ['initial_balance'=>$initial_balance->amount];
        else
            return ['initial_balance'=>"0"];
    }

    public function getDepositsCount(Request $request)
    {
        $deposits = Deposit::where('company_id', $request->company_id);

        $count = 0;
        if ($deposits)
            $count = $deposits->count();

        return ["code"  => "200",
                "count" => $count];
    }

    public function getAllDeposits(Request $request)
    {
        $deposits = Deposit::where('company_id', $request->company_id);

        //-----------------------------------------------------------------------

        if ($deposits->count()>0)
        {
            $deposits = $deposits->simplePaginate($request->paginate);

            $result = collect([]);

            $deposits = DepositResource1::collection($deposits);
            foreach ($deposits as $deposit)
            {
                $result->push(
                    [
                        'deposit'       => $deposit,
                        'account_title' => $deposit->account->title,]
                );
            }
            return ["code"   => "200",
                    "result" => $result];
        }

        return ["code"    => "207",
                'message' => trans('message1.207')];
    }

    public function searchQuery(Request $request)
    {
        $deposits = Deposit::where('company_id', $request->company_id)
                             ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                             ->get();

        //------------------------------------------------
        if ($deposits->count()>0)
        {

            $result = collect([]);

            $deposits = DepositResource1::collection($deposits);
            foreach ($deposits as $deposit)
            {
                $result->push(
                    [
                        'deposit'         => $deposit,
                        'account_title'    => $deposit->account->title,]
                );
            }
            return ["code"   => "200",
                    "result" => $result];
        }

        return ["code"    => "207",
                'message' => trans('message1.207')];
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
