<?php

namespace App\Http\Controllers;

use App\Models\Buyer;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class BuyerController extends Controller
{
    public function create(Request $request)
    {
        $buyer=Buyer::create(
            [
                'company_id'   => $request->company_id,
                'name'         => $request->name,
                'phone_number' => $request->phone_number,
                'destination'  => $request->destination,
            ]
        );

        return ['id'=>$buyer->id];
    }

    public function edit(Request $request){

        $buyer=Buyer::where('id',$request->buyer_id)->first();

        if ($buyer)
        {
            $buyer->name=$request->name;
            $buyer->phone_number=$request->phone_number;
            $buyer->destination=$request->destination;

            $buyer->save();

            return ['code'=>'200'];
        }

        return ['code' => '202', 'message' => trans('message1.202')];
    }

    public function delete(Request $request){

        $user = auth()->user();

        if (!Hash::check($request->password, $user->password))
            return ['code' => '205', 'message' => trans('message1.205')];

        else
        {
            foreach (($request->buyer_ids) as $buyer_id)
            {
                $buyer=Buyer::where('id',$buyer_id)->first();

                if ($buyer)
                    $buyer->delete();
            }

            return ['code'=>'200'];
        }

    }

    public function archive(Request $request){

        foreach (($request->buyer_ids) as $buyer_id)
        {
            $buyer=Buyer::where('id',$buyer_id)->first();

            if ($buyer)
            {
                $buyer->archive = "done";
                $buyer->save();
            }
        }
        return ['code'=>'200'];
    }

    public function unArchive(Request $request){

        foreach (($request->buyer_ids) as $buyer_id)
        {
            $buyer=Buyer::where('id',$buyer_id)->first();

            if ($buyer)
            {
                $buyer->archive = "";
                $buyer->save();
            }
        }
        return ['code'=>'200'];
    }

}
