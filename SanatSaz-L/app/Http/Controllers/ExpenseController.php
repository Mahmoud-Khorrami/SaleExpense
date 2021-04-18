<?php

namespace App\Http\Controllers;

use App\Models\Expense;
use App\Models\ExpenseDetail;
use Illuminate\Http\Request;

class ExpenseController extends Controller
{
    public function create(Request $request)
    {

        $user = auth()->user();

        $expense=Expense::create(
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

        foreach (($request->details) as $item)
        {
            $item = json_decode($item);

            ExpenseDetail::create(
                [
                    'user_id'     => $user->id,
                    'company_id'  => $request->company_id,
                    'expense_id'     => $expense->id,
                    'row'         => $item->row,
                    'description' => $item->description,
                    'number'      => $item->number,
                    'unit_price'  => $item->unitPrice,
                    'total_price' => $item->totalPrice,
                ]
            );

        }

        return ['code' => '200'];

    }

    public function getExpense(Request $request){

        $expense=Expense::where('id',$request->expense_id)->first();

        if ($expense)
        {
            return ['code'          => '200',
                    'expense'          => $expense,
                    'account_title' => $expense->account->title,
                    'expense_details'  => $expense->expenseDetails,
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

        $expense = Expense::where('id', $request->expense_id)->first();

        if ($expense)
        {

            $expense_details=$expense->expenseDetails;

            foreach ($expense_details as $item)
                $item->delete();

            //------------------------------------------------------------------

            $expense->user_id = $user->id;
            $expense->factor_number = $request->factor_number;
            $expense->date = $request->date;
            $expense->sum = $request->sum;
            $expense->payment = $request->payment;
            $expense->remain = $request->remain;
            $expense->account_id = $request->account_id;
            $expense->description = $request->description;

            $expense->save();

            //------------------------------------------------------------------

            foreach (($request->details) as $item)
            {
                $item = json_decode($item);

                ExpenseDetail::create(
                    [
                        'user_id'     => $user->id,
                        'company_id'  => $request->company_id,
                        'expense_id'  => $expense->id,
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
        $expense=Expense::where('id',$request->expense_id)->first();

        if ($expense)
            $expense->delete();

        return ['code'=>'200'];
    }
}
