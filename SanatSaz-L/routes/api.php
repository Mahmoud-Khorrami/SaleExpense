<?php

use App\Http\Controllers\BuyerController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\DepositController;
use App\Http\Controllers\DriverController;
use App\Http\Controllers\ExpenseController;
use App\Http\Controllers\AccountController;
use App\Http\Controllers\GeneralController;
use App\Http\Controllers\MaterialController;
use App\Http\Controllers\PersonnelController;
use App\Http\Controllers\SalaryController;
use App\Http\Controllers\SaleController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\WithdrawController;
use App\Models\Account;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/



Route::post('balance', [GeneralController::class,'balance']);
Route::middleware('secret_key')->group(function ()
{


    //Route::post('register', [UserController::class, 'register']);
    Route::post('login', [UserController::class, 'login']);

    //----------------------------------------------------------------------------------------

    Route::group(
        ['middleware' => 'auth:sanctum'], function ()
    {
        Route::prefix('company')->group(
            function ()
            {
                Route::post('create', [CompanyController::class, 'create']);
                Route::post('show', [CompanyController::class, 'show']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('user')->group(
            function ()
            {
                Route::post('register', [UserController::class, 'register']);
                Route::post('show', [UserController::class, 'show']);
                Route::post('edit', [UserController::class, 'edit']);
                Route::post('delete',[UserController::class,'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('sale')->group(
            function ()
            {
                Route::post('create', [SaleController::class, 'create']);
                Route::post('get-sale', [SaleController::class, 'getSale']);
                Route::post('edit', [SaleController::class, 'edit']);
                Route::post('delete', [SaleController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('deposit')->group(
            function ()
            {
                Route::post('create', [DepositController::class, 'create']);
                Route::post('initial-balance', [DepositController::class, 'initialBalance']);
                Route::post('get-deposit', [DepositController::class, 'getDeposit']);
                Route::post('edit', [DepositController::class, 'edit']);
                Route::post('delete', [DepositController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('account')->group(
            function ()
            {
                Route::post('create', [AccountController::class, 'create']);
                Route::post('edit', [AccountController::class, 'edit']);
                Route::post('delete', [AccountController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('buyer')->group(
            function ()
            {
                Route::post('create', [BuyerController::class, 'create']);
                Route::post('edit', [BuyerController::class, 'edit']);
                Route::post('delete', [BuyerController::class, 'delete']);
                Route::post('archive', [BuyerController::class, 'archive']);
                Route::post('un-archive', [BuyerController::class, 'unArchive']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('driver')->group(
            function ()
            {
                Route::post('create', [DriverController::class, 'create']);
                Route::post('edit', [DriverController::class, 'edit']);
                Route::post('delete', [DriverController::class, 'delete']);
                Route::post('archive', [DriverController::class, 'archive']);
                Route::post('un-archive', [DriverController::class, 'unArchive']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('personnel')->group(
            function ()
            {
                Route::post('create', [PersonnelController::class, 'create']);
                Route::post('edit', [PersonnelController::class, 'edit']);
                Route::post('delete', [PersonnelController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('salary')->group(
            function ()
            {
                Route::post('create', [SalaryController::class, 'create']);
                Route::post('edit', [SalaryController::class, 'edit']);
                Route::post('delete', [SalaryController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('general')->group(
            function ()
            {
                Route::post('data1', [GeneralController::class, 'data1']);
                Route::post('data2', [GeneralController::class, 'data2']);
                Route::post('data3', [GeneralController::class, 'data3']);
                Route::post('data4', [GeneralController::class, 'data4']);
                Route::post('data5', [GeneralController::class, 'data5']);
                Route::post('data6', [GeneralController::class, 'data6']);
                Route::post('data7', [GeneralController::class, 'data7']);
                Route::post('data8', [GeneralController::class, 'data8']);
                Route::post('data9', [GeneralController::class, 'data9']);
                Route::post('data10', [GeneralController::class, 'data10']);
                Route::post('data11', [GeneralController::class, 'data11']);
                Route::post('data12', [GeneralController::class, 'data12']);
                Route::post('data13', [GeneralController::class, 'data13']);
                Route::post('data14', [GeneralController::class, 'data14']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('expense')->group(
            function ()
            {
                Route::post('create', [ExpenseController::class, 'create']);
                Route::post('get-expense', [ExpenseController::class, 'getExpense']);
                Route::post('edit', [ExpenseController::class, 'edit']);
                Route::post('delete', [ExpenseController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('material')->group(
            function ()
            {
                Route::post('create', [MaterialController::class, 'create']);
                Route::post('get-material', [MaterialController::class, 'getMaterial']);
                Route::post('edit', [MaterialController::class, 'edit']);
                Route::post('delete', [MaterialController::class, 'delete']);
            }
        );

    }
    );

});
