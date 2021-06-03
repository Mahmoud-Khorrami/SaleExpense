<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class SlaryResource1 extends JsonResource
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
            "personnel_id"=>$this->personnel_id,
            "salary"=>$this->salary,
            "earnest"=>$this->earnest,
            "insurance_tax"=>$this->insurance_tax,
            "account_id"=>$this->account_id,
            "date"=>$this->date,
            "description"=>$this->description,
        ];
    }
}
