<?php

namespace App\Http\Controllers;

use App\Models\Account;
use App\Models\Material;
use App\Models\MaterialDetail;
use Illuminate\Http\Request;

class MaterialController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        $material=Material::create(
            [
                'user_id'       => $user->id,
                'company_id'    => $request->company_id,
                'factor_number' => $request->factor_number,
                'date'          => $request->date,
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

            MaterialDetail::create(
                [
                    'user_id'     => $user->id,
                    'company_id'  => $request->company_id,
                    'material_id' => $material->id,
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

    public function getMaterial(Request $request){

        $material=Material::where('id',$request->material_id)->first();

        if ($material)
        {
            return ['code'          => '200',
                    'material'          => $material,
                    'account_title' => $material->account->title,
                    'material_details'  => $material->materialDetails,
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

        $material = Material::where('id', $request->material_id)->first();

        if ($material)
        {

            $material_details=$material->materialDetails;

            foreach ($material_details as $item)
                $item->delete();

            //------------------------------------------------------------------

            $material->user_id = $user->id;
            $material->factor_number = $request->factor_number;
            $material->date = $request->date;
            $material->sum = $request->sum;
            $material->payment = $request->payment;
            $material->remain = $request->remain;
            $material->account_id = $request->account_id;
            $material->description = $request->description;

            $material->save();

            //------------------------------------------------------------------

            foreach (($request->details) as $item)
            {
                $item = json_decode($item);

                MaterialDetail::create(
                    [
                        'user_id'     => $user->id,
                        'company_id'  => $request->company_id,
                        'material_id' => $material->id,
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
        $material=Material::where('id',$request->material_id)->first();

        if ($material)
            $material->delete();

        return ['code'=>'200'];
    }
}
