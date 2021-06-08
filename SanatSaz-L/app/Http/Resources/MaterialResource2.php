<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class MaterialResource2 extends JsonResource
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
            "material_id"=>$this->material_id,
            "row"=> $this->row,
            "description"=> $this->description,
            "number"=> $this->number,
            "unit_price"=> $this->unit_price,
            "total_price"=> $this->total_price,
        ];
    }
}
