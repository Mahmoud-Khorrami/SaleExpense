<?php

namespace App\Http\Controllers;

use App\Models\Personnel;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class PersonnelController extends Controller
{
    public function create(Request $request)
    {

        $personnel = Personnel::create(
            [
                'company_id'    => $request->company_id,
                'name'          => $request->name,
                'phone_number'  => $request->phone_number,
                'register_date' => $request->register_date,
                'role'          => $request->role,
                'credit_card'   => $request->credit_card,
            ]
        );

        return ['id' => $personnel->id];
    }

    public function edit(Request $request){

        $personnel=Personnel::where('id',$request->personnel_id)->first();

        $personnel->name=$request->name;
        $personnel->phone_number=$request->phone_number;
        $personnel->register_date=$request->register_date;
        $personnel->role=$request->role;
        $personnel->credit_card=$request->credit_card;

        $personnel->save();

        return ['code'=>'200'];
    }

    public function delete(Request $request){

        $user = auth()->user();

        if (!Hash::check($request->password, $user->password))
            return ['code' => '205', 'message' => trans('message1.205')];

        else
        {
            $personnel=Personnel::where('id',$request->personnel_id)->first();

            if ($personnel)
                $personnel->delete();

            return ['code'=>'200'];
        }

    }
}
