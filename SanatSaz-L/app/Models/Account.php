<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Account extends Model
{
    use HasFactory;

    protected $fillable =[
      'user_id',
      'company_id',
      'title',
      'account_number',
      'balance'
    ];

    public function sales()
    {
        return $this->hasMany(Sale::class);
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function company()
    {
        return $this->belongsTo(Company::class);
    }

    public function deposits()
    {
        return $this->hasMany(Deposit::class);
    }

    public function salaries()
    {
        return $this->hasMany(Salary::class);
    }

    public function expenses()
    {
        return $this->hasMany(Expense::class);
    }

    public function materials()
    {
        return $this->hasMany(Material::class);
    }
}
