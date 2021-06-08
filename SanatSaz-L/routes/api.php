<?php

use App\Http\Controllers\BuyerController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\DepositController;
use App\Http\Controllers\DriverController;
use App\Http\Controllers\ExpenseController;
use App\Http\Controllers\AccountController;
use App\Http\Controllers\GeneralController;
use App\Http\Controllers\MaterialController;
use App\Http\Controllers\PaymentController;
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
                Route::post('archive', [UserController::class, 'archive']);
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
                Route::post('get-sales-count', [SaleController::class, 'getSalesCount']);
                Route::post('get-all-sales', [SaleController::class, 'getAllSales']);
                Route::post('get-buyer-sales', [SaleController::class, 'getBuyerSales']);
                Route::post('search-query', [SaleController::class, 'searchQuery']);
                Route::post('buyer-search-query', [SaleController::class, 'buyerSearchQuery']);
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
                Route::post('get-deposits-count', [DepositController::class, 'getDepositsCount']);
                Route::post('get-all-deposits', [DepositController::class, 'getAllDeposits']);
                Route::post('search-query', [DepositController::class, 'searchQuery']);
                Route::post('edit', [DepositController::class, 'edit']);
                Route::post('delete', [DepositController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('account')->group(
            function ()
            {
                Route::post('create', [AccountController::class, 'create']);
                Route::post('account-query1', [AccountController::class, 'accountsQuery1']);
                Route::post('archive', [AccountController::class, 'archive']);
                Route::post('edit', [AccountController::class, 'edit']);
                Route::post('delete', [AccountController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('buyer')->group(
            function ()
            {
                Route::post('create', [BuyerController::class, 'create']);
                Route::post('get-buyers', [BuyerController::class, 'getBuyers']);
                Route::post('search-query', [BuyerController::class, 'searchQuery']);
                Route::post('archive', [BuyerController::class, 'archive']);
                Route::post('edit', [BuyerController::class, 'edit']);
                Route::post('delete', [BuyerController::class, 'delete']);
                Route::post('un-archive', [BuyerController::class, 'unArchive']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('driver')->group(
            function ()
            {
                Route::post('create', [DriverController::class, 'create']);
                Route::post('get-drivers', [DriverController::class, 'getDrivers']);
                Route::post('search-query', [DriverController::class, 'searchQuery']);
                Route::post('archive', [DriverController::class, 'archive']);
                Route::post('edit', [DriverController::class, 'edit']);
                Route::post('delete', [DriverController::class, 'delete']);
                Route::post('un-archive', [DriverController::class, 'unArchive']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('personnel')->group(
            function ()
            {
                Route::post('create', [PersonnelController::class, 'create']);
                Route::post('personnel-query1', [PersonnelController::class, 'personnelQuery1']);
                Route::post('search-query', [PersonnelController::class, 'searchQuery']);
                Route::post('archive', [PersonnelController::class, 'archive']);
                Route::post('edit', [PersonnelController::class, 'edit']);
                Route::post('delete', [PersonnelController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('salary')->group(
            function ()
            {
                Route::post('create', [SalaryController::class, 'create']);
                Route::post('get-salaries-count', [SalaryController::class, 'getSalariesCount']);
                Route::post('get-all-salaries', [SalaryController::class, 'getAllSalaries']);
                Route::post('search-query', [SalaryController::class, 'searchQuery']);
                Route::post('edit', [SalaryController::class, 'edit']);
                Route::post('delete', [SalaryController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('general')->group(
            function ()
            {
                Route::post('xls', [GeneralController::class, 'xls']);
                Route::post('report1', [GeneralController::class, 'report1']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('expense')->group(
            function ()
            {
                Route::post('create', [ExpenseController::class, 'create']);
                Route::post('get-expense', [ExpenseController::class, 'getExpense']);
                Route::post('get-expenses-count', [ExpenseController::class, 'getExpensesCount']);
                Route::post('get-all-expenses', [ExpenseController::class, 'getAllExpenses']);
                Route::post('search-query', [ExpenseController::class, 'searchQuery']);
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
                Route::post('get-materials-count', [MaterialController::class, 'getMaterialsCount']);
                Route::post('get-all-materials', [MaterialController::class, 'getAllMaterials']);
                Route::post('search-query', [MaterialController::class, 'searchQuery']);
                Route::post('edit', [MaterialController::class, 'edit']);
                Route::post('delete', [MaterialController::class, 'delete']);
            }
        );

        //----------------------------------------------------------------------------------------

        Route::prefix('payment')->group(
            function ()
            {
                Route::post('create', [PaymentController::class, 'create']);
                Route::post('edit', [PaymentController::class, 'edit']);
                Route::post('delete', [PaymentController::class, 'delete']);
            }
        );
    }
    );

});
