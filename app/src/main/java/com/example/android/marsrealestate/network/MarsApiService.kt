/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://mars.udacity.com/"

//Defines constants to match the query values our web service expects
enum class MarsApiFilter(val value: String) { SHOW_RENT("rent"), SHOW_BUY("buy"), SHOW_ALL("all") }

//Retrofit builder with ConverterFactory and BASE_URL
/*private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()
  */

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())  //allows to replace the Call in getProperties() with a coroutine deferred
    .baseUrl(BASE_URL)
    .build()


/**
Interface that explains how Retrofit talks to the web server using HTTP requests
Retrofit will create an object that implements the interface with all of the methods that talk to the server
Conceptually similar the way Room implements DAO's*/

/*interface MarsApiService {
    @GET("realestate")                  //Annotating the method with @GET specifies the endpoint for the JSON real estate response
    fun getProperties(): Call<String>   //Retrofit Call object that will start the HTTP request.
}*/

interface MarsApiService {
    @GET("realestate")
    fun getProperties(@Query("filter") type: String):  //filter properties based on the MarsApiFilter enum values
    //Call<List<MarsProperty>>
            Deferred<List<MarsProperty>>
}

/**
Creating Retrofit calls are expensive, and this app only needs one Retrofit server instance.
Use a public object to expose the Retrofit service to the rest of the application
Calling MarsApi.retrofitService will return a retrofit object that implements MarsApiService*/


object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)

    }
}


