<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Company extends Model
{
    use HasFactory;

    protected $fillable =
        [
            'name',
            'date',
        ];
    public function users()
    {
        return $this->hasMany(User::class);
    }

    public function expenses()
    {
        return $this->hasMany(Expense::class);
    }

    public function sales()
    {
        return $this->hasMany(Sale::class);
    }

    public function deposits()
    {
        return $this->hasMany(Deposit::class);
    }

    public function buyers()
    {
        return $this->hasMany(Buyer::class);
    }

    public function drivers()
    {
        return $this->hasMany(Driver::class);
    }

    public function saleDetails()
    {
        return $this->hasMany(SaleDetail::class);
    }

    public function saleAccounts()
    {
        return $this->hasMany(Account::class);
    }

    public function personnel()
    {
        return $this->hasMany(Account::class);
    }

    public function salaries()
    {
        return $this->hasMany(Salary::class);
    }

    public function materials()
    {
        return $this->hasMany(Material::class);
    }

}
