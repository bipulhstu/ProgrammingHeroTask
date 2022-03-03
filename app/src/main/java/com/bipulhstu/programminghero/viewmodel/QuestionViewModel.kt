package com.bipulhstu.programminghero.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bipulhstu.programminghero.model.Question
import com.bipulhstu.programminghero.repository.QuestionRespository


class QuestionViewModel() : ViewModel() {

    var QuestionsLiveData: MutableLiveData<List<Question>>? = null

    //get questions from API
    fun getQuestions(): MutableLiveData<List<Question>>? {

        QuestionsLiveData = QuestionRespository().getQuestionsApiCall()

        return QuestionsLiveData
    }

}