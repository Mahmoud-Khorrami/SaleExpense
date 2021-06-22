<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class MaterialResource1 extends JsonResource
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
            "seller_id"=>$this->seller_id,
            "sum"=>$this->sum,
            "payment"=>$this->payment,
            "remain"=>$this->remain,
            "account_id"=>$this->account_id,
            "description"=>$this->description,
        ];
    }
}
