<?php

namespace App\Http\Controllers;

use App\Http\Resources\SlaryResource1;
use App\Models\Personnel;
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

    public function getSalariesCount(Request $request)
    {
        $salaries = Salary::where('company_id', $request->company_id);

        $count = 0 ;
        if ($salaries)
            $count = $salaries->count();

        return ["code"  => "200",
                "count" => $count];
    }

    public function getAllSalaries(Request $request)
    {
        $salaries = Salary::where('company_id', $request->company_id);

        //-----------------------------------------------------------------------

        if ($salaries->count()>0)
        {
            $salaries = $salaries->simplePaginate($request->paginate);

            $result = collect([]);

            $salaries = SlaryResource1::collection($salaries);
            foreach ($salaries as $salary)
            {
                $result->push(
                    [
                        'salary'         => $salary,
                        'account_title'  => $salary->account->title,
                        'personnel_name' => $salary->personnel->name,]
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
        if ($request->type == "date")
        {
            $salaries = Salary::where('company_id', $request->company_id)
                              ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                              ->get();

            //------------------------------------------------
            if ($salaries->count()>0)
            {

                $result = collect([]);

                $salaries = SlaryResource1::collection($salaries);
                foreach ($salaries as $salary)
                {
                    $result->push(
                        [
                            'salary'         => $salary,
                            'account_title'  => $salary->account->title,
                            'personnel_name' => $salary->personnel->name]
                    );
                }
                return ["code"   => "200",
                        "result" => $result];
            }

            return ["code"    => "207",
                    'message' => trans('message1.207')];
        }

        else if ($request->type == "personnel_name")
        {
            $personnel = Personnel::where('company_id', $request->company_id)
                           ->where("name", 'LIKE', '%' . ($request->value) . '%')
                           ->get();

            //------------------------------------------------
            if ($personnel->count()>0)
            {
                $result = collect([]);

                foreach ($personnel as $item)
                {
                    $salaries = SlaryResource1::collection($item->salaries);
                    foreach ($salaries as $salary)
                    {
                        $result->push(
                            [
                                'salary'         => $salary,
                                'account_title'  => $salary->account->title,
                                'personnel_name' => $salary->personnel->name]
                        );
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
