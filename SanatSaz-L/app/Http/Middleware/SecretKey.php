<?php

namespace App\Http\Middleware;

use App\Http\Requests\RegisterRequest;
use Closure;
use Illuminate\Http\Request;

class SecretKey
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle(Request $request, Closure $next)
    {

        if ($request->secret_key=='Aswed-BGfds-deAjh-W582fr-Prf65')
            return $next($request);

        else
        {
            $response = ['code' => '110', 'message' => trans('message.110')];

            return response()->json($response);
        }
    }



}
