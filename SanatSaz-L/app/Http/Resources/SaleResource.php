<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class SaleResource extends JsonResource
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
            "factor_number"=>$this->factor_number,
            "date"=>$this->date,
            "buyer_id"=>$this->buyer_id,
            "driver_id"=>$this->driver_id,
            "sum"=>$this->sum,
            "payment"=>$this->payment,
            "account_id"=>$this->account_id,
            "description"=>$this->description,
        ];
    }
}
