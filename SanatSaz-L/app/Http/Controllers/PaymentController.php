<?php

namespace App\Http\Controllers;

use App\Models\Payment;
use Illuminate\Http\Request;

class PaymentController extends Controller
{

    public function create(Request $request)
    {

        Deposit::create(
            [
                'company_id'  => $request->company_id,
                'sale_id'       => $request->sale_id,
                'amount'      => $request->amount,
                'date'        => $request->date,
                'description' => $request->description
            ]
        );

        return ['code' => '200'];
    }

    public function edit(Request $request)
    {
        $payment = Payment::where('id', $request->payment_id)->first();

        if ($payment)
        {
            $payment->amount = $request->amount;
            $payment->date = $request->date;
            $payment->description = $request->description;

            $payment->save();
        }

        return ['code'=>'200'];
    }

    public function delete(Request $request)
    {
        $payment=Payment::where('id',$request->payment_id)->first();

        if ($payment)
            $payment->delete();

        return ['code'=>'200'];
    }
}
