<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class PersonnelResource1 extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array
     */
    public function toArray($request)
    {
        return [
            "id"=> $this->id,
            "name"=>$this->name,
            "phone_number"=>$this->phone_number,
            "register_date"=>$this->register_date,
            "role"=>$this->role,
            "credit_card"=>$this->credit_card,
        ];
    }
}
