<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Material extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'company_id',
        'factor_number',
        'date',
        'seller_id',
        'sum',
        'payment',
        'remain',
        'account_id',
        'description',
    ];

    public function materialDetails()
    {
        return $this->hasMany(MaterialDetail::class);
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function company()
    {
        return $this->belongsTo(Company::class);
    }

    public function account()
    {
        return $this->belongsTo(Account::class);
    }

    public function seller()
    {
        return $this->belongsTo(Seller::class);
    }
}
