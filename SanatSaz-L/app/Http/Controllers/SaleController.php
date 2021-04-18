<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Company;
use App\Models\Sale;
use App\Models\SaleDetail;
use App\Models\Withdraw;
use Illuminate\Http\Request;

class SaleController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        $sale=Sale::create(
            [
                'user_id'       => $user->id,
                'company_id'    => $request->company_id,
                'factor_number' => $request->factor_number,
                'date'          => $request->date,
                'buyer_id'      => $request->buyer_id,
                'driver_id'     => $request->driver_id,
                'sum'           => $request->sum,
                'payment'       => $request->payment,
                'remain'        => $request->remain,
                'account_id'    => $request->account_id,
                'description'   => $request->description,
            ]
        );

        //--------------------------------------------------------------

        foreach (($request->details) as $item)
        {
            $item = json_decode($item);

            SaleDetail::create(
                [
                    'user_id'     => $user->id,
                    'company_id'  => $request->company_id,
                    'sale_id'     => $sale->id,
                    'row'         => $item->row,
                    'description' => $item->description,
                    'number'      => $item->number,
                    'unit_price'  => $item->unitPrice,
                    'total_price' => $item->totalPrice,
                ]
            );

        }

        //--------------------------------------------------------------

        return ['code' => '200'];

    }

    public function getSale(Request $request){

        $sale=Sale::where('id',$request->sale_id)->first();

        if ($sale)
        {

            return ['code'          => '200',
                    'sale'          => $sale,
                    'buyer_name'    => $sale->buyer->name,
                    'driver_name'   => $sale->driver->name,
                    'account_title' => $sale->account->title,
                    'sale_details'  => $sale->saleDetails,
            ];
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

        $sale = Sale::where('id', $request->sale_id)->first();

        if ($sale)
        {

            $sale_details=$sale->saleDetails;

            foreach ($sale_details as $item)
                $item->delete();

            //------------------------------------------------------------------

            $sale->user_id = $user->id;
            $sale->factor_number = $request->factor_number;
            $sale->date = $request->date;
            $sale->buyer_id = $request->buyer_id;
            $sale->driver_id = $request->driver_id;
            $sale->sum = $request->sum;
            $sale->payment = $request->payment;
            $sale->remain = $request->remain;
            $sale->account_id = $request->account_id;
            $sale->description = $request->description;

            $sale->save();

            //------------------------------------------------------------------

            foreach (($request->details) as $item)
            {
                $item = json_decode($item);

                SaleDetail::create(
                    [
                        'user_id'     => $user->id,
                        'company_id'  => $request->company_id,
                        'sale_id'     => $sale->id,
                        'row'         => $item->row,
                        'description' => $item->description,
                        'number'      => $item->number,
                        'unit_price'  => $item->unitPrice,
                        'total_price' => $item->totalPrice,
                    ]
                );

            }

        }

        else
            $this->create($request);

        return ['code'=>'200'];

    }

    public function delete(Request $request)
    {
        $sale=Sale::where('id',$request->sale_id)->first();

        if ($sale)
            $sale->delete();

        return ['code'=>'200'];
    }
}
