<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class MaterialDetail extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'company_id',
        'material_id',
        'row',
        'description',
        'number',
        'unit_price',
        'total_price',
    ];

    public function material()
    {
        return $this->belongsTo(Material::class);
    }

    public function user()
    {
        return $this->belongsTo(User::class);
    }

    public function Company()
    {
        return $this->belongsTo(Company::class);
    }
}
