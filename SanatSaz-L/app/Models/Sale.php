<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Sale extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'company_id',
        'factor_number',
        'date',
        'buyer_id',
        'driver_id',
        'sum',
        'payment',
        'remain',
        'account_id',
        'description',
    ];

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function company()
    {
        return $this->belongsTo(Company::class);
    }

    public function buyer()
    {
        return $this->belongsTo(Buyer::class);
    }

    public function driver()
    {
        return $this->belongsTo(Driver::class);
    }

    public function account()
    {
        return $this->belongsTo(Account::class);
    }

    public function saleDetails()
    {
        return $this->hasMany(SaleDetail::class);
    }

}
