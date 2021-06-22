<?php

namespace App\Http\Controllers;

use App\Http\Resources\MaterialResource1;
use App\Http\Resources\MaterialResource2;
use App\Models\Account;
use App\Models\Material;
use App\Models\MaterialDetail;
use App\Models\Seller;
use Illuminate\Http\Request;

class MaterialController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        $material = Material::create(
            [
                'user_id'       => $user->id,
                'company_id'    => $request->company_id,
                'factor_number' => $request->factor_number,
                'date'          => $request->date,
                'seller_id'     => $request->seller_id,
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
                    'material_details'  => MaterialResource2::collection($material->materialDetails),
            ];
        }
        else
            return [
                'code'    => '202',
                'message' => trans('message1.202')
            ];
    }

    public function getMaterialsCount(Request $request){

        $user = auth()->user();
        $materials = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $materials = Material::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $materials = Material::where('company_id', $request->company_id)->where("user_id", $user->id);

        $count = 0;
        if ($materials)
            $count = $materials->count();

        return ["code"  => "200",
                "count" => $count];
    }

    public function getAllMaterials(Request $request)
    {
        $user = auth()->user();
        $materials = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $materials = Material::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $materials = Material::where('company_id', $request->company_id)->where("user_id", $user->id);

        //-----------------------------------------------------------------------

        if ($materials->count()>0)
        {
            $materials = $materials->simplePaginate($request->paginate);

            $result = collect([]);

            $materials = MaterialResource1::collection($materials);
            foreach ($materials as $material)
            {
                $result->push(
                    [
                        'material'         => $material,
                        'seller_name'      => $material->seller->seller_name,
                        'account_title'    => $material->account->title,
                        'material_details' => MaterialResource2::collection($material->materialDetails)]
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
        $materials = null;

        if ($request->type == "factor_number" || $request->type == "date")
        {
            if ($user->role == "مدیر" || $user->role == "Developer")
                $materials = Material::where('company_id', $request->company_id)
                                     ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                                     ->get();


            else if ($user->role == "کارمند")
                $materials = Material::where('company_id', $request->company_id)
                                     ->where('user_id', $user->id)
                                     ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                                     ->get();

            //------------------------------------------------
            if ($materials->count() > 0)
            {

                $result = collect([]);

                $materials = MaterialResource1::collection($materials);
                foreach ($materials as $material)
                {
                    $result->push(
                        [
                            'material'         => $material,
                            'seller_name'      => $material->seller->seller_name,
                            'account_title'    => $material->account->title,
                            'material_details' => MaterialResource2::collection($material->materialDetails)]
                    );
                }
                return ["code"   => "200",
                        "result" => $result];
            }

            return ["code"    => "207",
                    'message' => trans('message1.207')];
        }

        else if ($request->type == "seller_name")
        {
            $sellers = Seller::where('company_id', $request->company_id)
                           ->where("seller_name", 'LIKE', '%' . ($request->value) . '%')
                           ->get();

            //------------------------------------------------
            if ($sellers->count()>0)
            {
                $result = collect([]);

                foreach ($sellers as $seller)
                {
                    $materials = MaterialResource1::collection($seller->materials);
                    foreach ($materials as $material)
                    {

                        if ($user->role == "مدیر" || $user->role == "Developer")
                        {
                            $result->push(
                                [
                                    'material'         => $material,
                                    'seller_name'      => $material->seller->seller_name,
                                    'account_title'    => $material->account->title,
                                    'material_details' => MaterialResource2::collection($material->materialDetails)]
                            );
                        }

                        else if ($user->role == "کارمند")
                        {
                            if ($material->user_id == $user->id)
                            {
                                $result->push(
                                    [
                                        'material'         => $material,
                                        'seller_name'      => $material->seller->seller_name,
                                        'account_title'    => $material->account->title,
                                        'material_details' => MaterialResource2::collection($material->materialDetails)]
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
            $material->seller_id = $request->seller_id;
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
