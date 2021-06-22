<?php

namespace App\Http\Controllers;

use App\Models\Seller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class SellerController extends Controller
{
    public function create(Request $request)
    {
        $seller=Seller::create(
            [
                'company_id'   => $request->company_id,
                'seller_name'         => $request->seller_name,
                'shop_name' => $request->shop_name,
                'phone_number'  => $request->phone_number,
                'address'  => $request->address,
            ]
        );

        return ["code"    => "200",
                "result" => $seller->id];
    }

    public function getSellers(Request $request)
    {
        $seller = Seller::where('company_id', $request->company_id)
                      ->whereNull("archive")
                      ->get();

        if (($seller->count()) > 0)
        {
            return ["code"   => "200",
                    "result" => $seller];
        }

        return ["code"    => "207",
                'message' => trans('message1.207')];

    }

    public function searchQuery(Request $request)
    {
        $seller = Seller::where('company_id', $request->company_id)
                      ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                      ->whereNull("archive")
                      ->get();

        return ["code"   => "200",
                "result" => $seller];
    }

    public function edit(Request $request){

        $seller=Seller::where('id',$request->seller_id)->first();

        if ($seller)
        {
            $seller->seller_name=$request->seller_name;
            $seller->shop_name=$request->shop_name;
            $seller->phone_number=$request->phone_number;
            $seller->address=$request->address;

            $seller->save();

            return ['code'=>'200'];
        }

        return ['code' => '202', 'message' => trans('message1.202')];
    }

    public function archive(Request $request){

        foreach (($request->seller_ids) as $seller_id)
        {
            $seller=Seller::where('id',$seller_id)->first();

            if ($seller)
            {
                $seller->archive = "done";
                $seller->save();
            }
        }
        return ['code'=>'200'];
    }

    public function unArchive(Request $request){

        foreach (($request->seller_ids) as $seller_id)
        {
            $seller=Seller::where('id',$seller_id)->first();

            if ($seller)
            {
                $seller->archive = "";
                $seller->save();
            }
        }
        return ['code'=>'200'];
    }
}
