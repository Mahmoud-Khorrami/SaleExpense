<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Deposit;
use App\Models\Salary;
use Illuminate\Http\Request;

class SalaryController extends Controller
{
    public function create(Request $request)
    {

        Salary::create(
            [
                'company_id'    => $request->company_id,
                'personnel_id'  => $request->personnel_id,
                'salary'        => $request->salary,
                'earnest'       => $request->earnest,
                'insurance_tax' => $request->insurance_tax,
                'account_id'    => $request->account_id,
                'date'          => $request->date,
                'description'   => $request->description,
            ]
        );

        return ['code' => '200'];
    }

    public function edit(Request $request)
    {
        $salary = Salary::where('id', $request->id)->first();

        if ($salary)
        {
            $salary->personnel_id = $request->personnel_id;
            $salary->salary = $request->salary;
            $salary->earnest = $request->earnest;
            $salary->insurance_tax = $request->insurance_tax;
            $salary->account_id = $request->account_id;
            $salary->date = $request->date;
            $salary->description = $request->description;

            $salary->save();
        }

        else
            $this->create($request);

        return ['code' => '200'];
    }

    public function delete(Request $request)
    {
        $salary = Salary::where('id', $request->id)->first();

        if ($salary)
            $salary->delete();

        return ['code' => '200'];
    }
}
