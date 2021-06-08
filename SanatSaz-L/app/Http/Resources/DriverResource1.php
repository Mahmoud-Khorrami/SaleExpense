<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class DriverResource1 extends JsonResource
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
            "car_type"=>$this->car_type,
            "number_plate"=>$this->number_plate
        ];
    }
}
