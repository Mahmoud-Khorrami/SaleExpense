<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Payment extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'company_id',
        'amount',
        'date',
        'description',
    ];

    public function sale()
    {
        return $this->belongsTo(Sale::class);
    }

}
