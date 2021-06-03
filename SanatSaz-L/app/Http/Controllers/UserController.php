<?php

namespace App\Http\Controllers;

use App\Http\Resources\LoginResource;
use App\Models\Company;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class UserController extends Controller
{
    public function register(Request $request)
    {

        $user = User::where('Phone_number', $request->phone_number)->first();

        if (!$user)
        {
            User::Create(
                [
                    'name'         => $request->name,
                    'phone_number' => $request->phone_number,
                    'role'         => $request->role,
                    'company_id'   => $request->company_id,
                    'password'     => Hash::make($request->password),
                ]
            );

            return ['code' => '200',];
        }

        return ['code'    => '203',
                'message' => trans('message1.203')];
    }

    public function login(Request $request)
    {

        $user = User::where('phone_number', $request->phone_number)->first();

        if (!$user)
            return ['code' => '204', 'message' => trans('message1.204')];

        else if (!Hash::check($request->password, $user->password))
            return ['code' => '205', 'message' => trans('message1.205')];

        else
        {
            $company_name = null;

            if (($user->company_id) != null)
            {
                $company = Company::where('id', $user->company_id)->first();
                $company_name = $company->name;
            }

            return ['code' => '200',
                    'user' => ['user_id'      => $user->id,
                               'phone_number' => $user->phone_number,
                               'name'         => $user->name,
                               'role'         => $user->role,
                               'company_id'   => $user->company_id,
                               'company_name' => $company_name],

                    'token' => $user->createToken($request->phone_number)->toArray(),];
        }

    }

    public function archive(Request $request)
    {
        $user = Usera::where('id', $request->user_id)->first();

        if ($user)
        {
            $user->archive = "done";
            $user->save();
        }

        return ['code' => '200'];
    }

    public function show(Request $request)
    {
        $users = User::where('company_id', $request->company_id)
                     ->whereNull("archive")
                     ->get();

        return ['users' => $users,];
    }

    public function edit(Request $request)
    {
        $user=User::where('id',$request->user_id)->first();

        if ($user)
        {
            $users = User::where('phone_number', $request->phone_number)->get();

            foreach ($users as $user)
            {
                if ($user->id != $request->user_id)
                    return ['code'    => '206',
                            'message' => trans('message1.206')];
            }

            $user->name = $request->name;
            $user->phone_number = $request->phone_number;
            $user->role = $request->role;
            $user->password = Hash::make($request->password);

            $user->save();

            return ['code' => '200'];
        }

        return ['code'    => '204',
                'message' => trans('message1.204')];


    }

    public function delete(Request $request){

        $user = auth()->user();

        if (!Hash::check($request->password, $user->password))
            return ['code' => '205', 'message' => trans('message1.205')];

        else
        {
            $user=User::where('id',$request->user_id)->first();

            if ($user)
                $user->delete();

            return ['code'=>'200'];
        }

    }
}
