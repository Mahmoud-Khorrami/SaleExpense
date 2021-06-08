<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class SaleResource3 extends JsonResource
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
            "sale_id"=>$this->sale_id,
            "row"=> $this->row,
            "description"=> $this->description,
            "number"=> $this->number,
            "unit_price"=> $this->unit_price,
            "total_price"=> $this->total_price,
        ];
    }
}
