<?php

namespace App\Http\Controllers;

use App\Models\Company;
use Illuminate\Http\Request;

class CompanyController extends Controller
{
    public function create(Request $request)
    {

        $company=Company::where('name',$request->name)->first();

        if (!$company)
        {
            Company::create(
                [
                    'name'           => $request->name,
                    'date'           => $request->date,
                ]
            );

            return ['code'    => '200'];
        }

        return [
            'code'    => '201',
            'message' => trans('message1.201'),
        ];

    }

    public function show(){

        $companies=Company::all();

        return ['companies'=>$companies];
    }
}
