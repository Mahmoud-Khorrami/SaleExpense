<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class DepositResource1 extends JsonResource
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
            "title"=>$this->title,
            "amount"=>$this->amount,
            "account_id"=>$this->account_id,
            "date"=>$this->date,
            "description"=>$this->description,
        ];
    }
}
