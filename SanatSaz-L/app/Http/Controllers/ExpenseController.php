<?php

namespace App\Http\Controllers;

use App\Http\Resources\ExpenseResource1;
use App\Http\Resources\ExpenseResource2;
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
                    'expense_details'  => ExpenseResource2::collection($expense->expenseDetails),
            ];
        }
        else
            return [
                'code'    => '202',
                'message' => trans('message1.202')
            ];
    }

    public function getExpensesCount(Request $request)
    {

        $user = auth()->user();
        $expenses = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $expenses = Expense::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $expenses = Expense::where('company_id', $request->company_id)->where("user_id", $user->id);

        $count = 0;
        if ($expenses)
            $count = $expenses->count();

        return ["code"  => "200",
                "count" => $count];
    }

    public function getAllExpenses(Request $request)
    {
        $user = auth()->user();
        $expenses = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $expenses = Expense::where('company_id', $request->company_id);

        else if ($user->role == "کارمند")
            $expenses = Expense::where('company_id', $request->company_id)->where("user_id", $user->id);

        //-----------------------------------------------------------------------

        if ($expenses)
        {
            $expenses = $expenses->simplePaginate($request->paginate);

            $result = collect([]);

            $expenses = ExpenseResource1::collection($expenses);
            foreach ($expenses as $expens)
            {
                $result->push(
                    [
                        'expense'         => $expens,
                        'account_title'   => $expens->account->title,
                        'expense_details' => ExpenseResource2::collection($expens->expenseDetails)]
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
        $expenses = null;

        if ($user->role == "مدیر" || $user->role == "Developer")
            $expenses = Expense::where('company_id', $request->company_id)
                               ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                               ->get();


        else if ($user->role == "کارمند")
            $expenses = Expense::where('company_id', $request->company_id)
                               ->where('user_id', $user->id)
                               ->where(($request->type), 'LIKE', '%' . ($request->value) . '%')
                               ->get();

        //------------------------------------------------
        if ($expenses)
        {

            $result = collect([]);

            $expenses = ExpenseResource1::collection($expenses);
            foreach ($expenses as $expens)
            {
                $result->push(
                    [
                        'expense'          => $expens,
                        'account_title' => $expens->account->title,
                        'expense_details' => ExpenseResource2::collection($expens->expenseDetails)]
                );
            }
            return ["code"   => "200",
                    "result" => $result];
        }

        return ["code"    => "207",
                'message' => trans('message1.207')];

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
