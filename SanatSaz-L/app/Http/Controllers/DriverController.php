<?php

namespace App\Http\Controllers;

use App\Models\Driver;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class DriverController extends Controller
{
    public function create(Request $request)
    {
        $driver = Driver::create(
            [
                'company_id'   => $request->company_id,
                'name'         => $request->name,
                'phone_number' => $request->phone_number,
                'car_type'     => $request->car_type,
                'number_plate' => $request->number_plate,
                'archive'      => $request->archive,
            ]
        );

        return ['id' => $driver->id];
    }

    public function edit(Request $request){

        $driver=Driver::where('id',$request->driver_id)->first();

        if ($driver)
        {
            $driver->name=$request->name;
            $driver->phone_number=$request->phone_number;
            $driver->car_type=$request->car_type;
            $driver->number_plate=$request->number_plate;

            $driver->save();

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
            foreach (($request->driver_ids) as $driver_id)
            {
                $driver=Driver::where('id',$driver_id)->first();

                if ($driver)
                    $driver->delete();
            }


            return ['code'=>'200'];
        }

    }

    public function archive(Request $request){

        foreach (($request->driver_ids) as $driver_id)
        {
            $driver=Driver::where('id',$driver_id)->first();

            if ($driver)
            {
                $driver->archive = "done";
                $driver->save();
            }
        }
        return ['code'=>'200'];
    }

    public function unArchive(Request $request){

        foreach (($request->driver_ids) as $driver_id)
        {
            $driver=Driver::where('id',$driver_id)->first();

            if ($driver)
            {
                $driver->archive = "";
                $driver->save();
            }
        }
        return ['code'=>'200'];
    }
}
