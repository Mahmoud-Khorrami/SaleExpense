<?php

namespace App\Http\Controllers;

use App\Http\Resources\SaleRecource2;
use App\Http\Resources\SaleResource;
use App\Models\Buyer;
use App\Models\Driver;
use App\Models\Sale;
use App\Models\SaleDetail;
use App\Models\Withdraw;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

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

    public function getSale(Request $request)
    {

        $sale = Sale::where('id', $request->sale_id)->first();

        if ($sale)
        {
            return ['code'        => '200',
                    'sale_detail' => SaleRecource2::collection($sale->saleDetails)];
        }
        else
            return [
                'code'    => '202',
                'message' => trans('message1.202')
            ];
    }

    public function getSalesCount(Request $request)
    {
        $user = Auth::user();
        $sales = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $sales = Sale::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $sales = Sale::where('company_id', $request->company_id)->where("user_id", $user->id);

        $count=0;
        if ($sales)
            $count = $sales->count();

        return ["code"  => "200",
                "count" => $count,
                "role"  => $user->id,];
    }

    public function getAllSales(Request $request)
    {

        $user = auth() -> user();
        $sales = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $sales = Sale::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $sales = Sale::where('company_id', $request->company_id)->where("user_id", $user->id);

        //-----------------------------------------------------------------------

        if ($sales)
        {
            $sales = $sales->simplePaginate($request->paginate);

            $result = collect([]);

            $sales = SaleResource::collection($sales);
            foreach ($sales as $sale)
            {
                $result->push(
                    [
                        'sale'          => $sale,
                        'buyer_name'    => $sale->buyer->name,
                        'driver_name'   => $sale->driver->name,
                        'account_title' => $sale->account->title,
                        'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
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
        $user = auth()->user();
        $sales = null;

        if ($request->type == "factor_number" || $request->type == "date")
        {

            if ($user->role == "مدیر" || $user->role == "Developer")
                $sales = Sale::where('company_id', $request->company_id)
                             ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                             ->get();


            else if ($user->role == "کارمند")
                $sales = Sale::where('company_id', $request->company_id)
                             ->where('user_id', $user->id)
                             ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                             ->get();

            //------------------------------------------------
            if ($sales)
            {

                $result = collect([]);

                $sales = SaleResource::collection($sales);
                foreach ($sales as $sale)
                {
                    $result->push(
                        [
                            'sale'          => $sale,
                            'buyer_name'    => $sale->buyer->name,
                            'driver_name'   => $sale->driver->name,
                            'account_title' => $sale->account->title,
                            'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
                    );
                }
                return ["code"   => "200",
                        "result" => $result];
            }

            return ["code"    => "207",
                    'message' => trans('message1.207')];

        }

        else if ($request->type == "buyer_name")
        {
            $buyers = Buyer::where('company_id', $request->company_id)
                           ->where("name", 'LIKE', '%' . ($request->value) . '%')
                           ->get();

            //------------------------------------------------
            if ($buyers)
            {
                $result = collect([]);

                foreach ($buyers as $buyer)
                {
                    $sales = SaleResource::collection($buyer->sales);
                    foreach ($sales as $sale)
                    {

                        if ($user->role == "مدیر" || $user->role == "Developer")
                        {
                            $result->push(
                                [
                                    'sale'          => $sale,
                                    'buyer_name'    => $sale->buyer->name,
                                    'driver_name'   => $sale->driver->name,
                                    'account_title' => $sale->account->title,
                                    'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
                            );
                        }

                        else if ($user->role == "کارمند")
                        {
                            if ($sale->user_id == $user->id)
                            {
                                $result->push(
                                    [
                                        'sale'          => $sale,
                                        'buyer_name'    => $sale->buyer->name,
                                        'driver_name'   => $sale->driver->name,
                                        'account_title' => $sale->account->title,
                                        'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
                                );
                            }
                        }
                    }
                }
                return ["code"   => "200",
                        "result" => $result];
            }

            return ["code"    => "207",
                    'message' => trans('message1.207')];


        }

        else if ($request->type == "driver_name")
        {
            $drivers = Driver::where('company_id', $request->company_id)
                             ->where("name", 'LIKE', '%' . ($request->value) . '%')
                             ->get();

            //------------------------------------------------
            if ($drivers)
            {
                $result = collect([]);

                foreach ($drivers as $driver)
                {
                    $sales = SaleResource::collection($driver->sales);
                    foreach ($sales as $sale)
                    {

                        if ($user->role == "مدیر" || $user->role == "Developer")
                        {
                            $result->push(
                                [
                                    'sale'          => $sale,
                                    'buyer_name'    => $sale->buyer->name,
                                    'driver_name'   => $sale->driver->name,
                                    'account_title' => $sale->account->title,
                                    'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
                            );
                        }

                        else if ($user->role == "کارمند")
                        {
                            if ($sale->user_id == $user->id)
                            {
                                $result->push(
                                    [
                                        'sale'          => $sale,
                                        'buyer_name'    => $sale->buyer->name,
                                        'driver_name'   => $sale->driver->name,
                                        'account_title' => $sale->account->title,
                                        'sale_details'  => SaleRecource2::collection($sale->saleDetails)]
                                );
                            }
                        }
                    }
                }
                return ["code"   => "200",
                        "result" => $result];
            }

            return ["code"    => "207",
                    'message' => trans('message1.207')];
        }

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
