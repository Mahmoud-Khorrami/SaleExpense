<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Salary extends Model
{
    use HasFactory;

    protected $fillable = [
        'company_id',
        'personnel_id',
        'salary',
        'earnest',
        'insurance_tax',
        'account_id',
        'date',
        'description',
    ];

    public function company()
    {
        return $this->belongsTo(Company::class);
    }

    public function personnel()
    {
        return $this->belongsTo(Personnel::class);
    }

    public function account()
    {
        return $this->belongsTo(Account::class);
    }
}
