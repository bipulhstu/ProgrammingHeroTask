package com.bipulhstu.programminghero.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bipulhstu.programminghero.model.Question
import com.bipulhstu.programminghero.model.QuestionListResponse
import com.bipulhstu.programminghero.retrofit.ApiConfig
import com.bipulhstu.programminghero.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionRespository {

    val questionList = MutableLiveData<List<Question>>()

    // get questions from API
    fun getQuestionsApiCall(): MutableLiveData<List<Question>> {

        val call = RetrofitClient.getInstance().create(ApiConfig::class.java).getQuestionList()

        call.enqueue(object : Callback<QuestionListResponse> {
            override fun onResponse(
                call: Call<QuestionListResponse>,
                response: Response<QuestionListResponse>
            ) {
                val body = response.body()
                if (body != null) {
                    questionList.value = response.body()!!.questions
                }
            }

            override fun onFailure(call: Call<QuestionListResponse>, t: Throwable) {
            }
        })

        return questionList
    }


}