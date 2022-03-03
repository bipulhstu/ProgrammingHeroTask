package com.bipulhstu.programminghero.retrofit

import com.bipulhstu.programminghero.model.QuestionListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiConfig {

    @GET("quiz.json")
    fun getQuestionList(): Call<QuestionListResponse>

}