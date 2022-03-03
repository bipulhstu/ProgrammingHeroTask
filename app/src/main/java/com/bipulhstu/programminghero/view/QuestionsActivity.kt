package com.bipulhstu.programminghero.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.bipulhstu.programminghero.R
import com.bipulhstu.programminghero.databinding.ActivityQuestionsBinding
import com.bipulhstu.programminghero.model.Question
import com.bipulhstu.programminghero.model.QuestionListResponse
import com.bipulhstu.programminghero.retrofit.ApiConfig
import com.bipulhstu.programminghero.retrofit.ClientInstance
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionsBinding
    lateinit var questionList: List<Question>
    private lateinit var question: Question
    private lateinit var countDownTimer: CountDownTimer
    var progressCount = 0
    var index = 0
    var currentQuestion = 0
    var gainedScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getQuestionList()

        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressCount++
                //binding.progressBar.setProgress(progressCount)
            }

            override fun onFinish() {
                //Toast.makeText(this@QuestionsActivity, "Working", Toast.LENGTH_SHORT).show()

                index++
                currentQuestion = index+1
                if(index < questionList.size) setQuestion(questionList[index])
            }
        }

        binding.optionAButton.setOnClickListener {
            checkAnswer(binding.optionAButton, "A")
        }
        binding.optionBButton.setOnClickListener {
            checkAnswer(binding.optionBButton, "B")
        }
        binding.optionCButton.setOnClickListener {
            checkAnswer(binding.optionCButton, "C")
        }
        binding.optionDButton.setOnClickListener {
            checkAnswer(binding.optionDButton, "D")
        }

    }

    private fun checkAnswer(selectedOption: ConstraintLayout, selectedAnswer: String) {
        disableOption()
        if (question.correctAnswer == selectedAnswer) {
            selectedOption.setBackgroundResource(R.drawable.right_answer_bg)
            gainedScore += gainedScore + questionList[index].score
        } else {
            binding.optionAButton.setBackgroundResource(if ("A" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionBButton.setBackgroundResource(if ("B" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionCButton.setBackgroundResource(if ("C" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionDButton.setBackgroundResource(if ("D" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            selectedOption.setBackgroundResource(R.drawable.wrong_answer_bg)
        }
        Handler().postDelayed({
            index++
            currentQuestion = index+1
            if(index < questionList.size) setQuestion(questionList[index])
        }, 2000)
    }

    private fun getQuestionList() {
        val config = ClientInstance().getRetrofitInstance()!!.create(ApiConfig::class.java)
        val call: Call<QuestionListResponse> = config.getQuestionList()
        call.enqueue(object : Callback<QuestionListResponse> {
            override fun onResponse(
                call: Call<QuestionListResponse>,
                response: Response<QuestionListResponse>
            ) {
                if (response.isSuccessful) {
                    questionList = response.body()!!.questions
                    //shuffle(questionList)
                    currentQuestion = index+1
                    binding.progressBar.visibility = View.VISIBLE
                    setQuestion(questionList[index])
                }
            }

            override fun onFailure(call: Call<QuestionListResponse>, t: Throwable) {
                Log.d("error", t.message!!)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(question: Question) {
        this.question = question
        binding.questionCount.text = "Question: $currentQuestion/" + questionList.size
        binding.score.text = "Score: $gainedScore"
        binding.questionPoint.text = question.score.toString() + " Point"
        binding.mainQuestion.text = question.question


            binding.questionImage.visibility =if (question.questionImageUrl == null) View.INVISIBLE else View.VISIBLE
            Picasso.with(this).load(question.questionImageUrl).into(binding.questionImage)


        binding.optionA.text = question.answers.A
        binding.optionB.text = question.answers.B
        binding.optionC.text = question.answers.C
        binding.optionD.text = question.answers.D

         binding.optionAButton.visibility = if(binding.optionA.text.toString().isEmpty()) View.GONE else View.VISIBLE
         binding.optionBButton.visibility = if(binding.optionB.text.toString().isEmpty()) View.GONE else View.VISIBLE
         binding.optionCButton.visibility = if(binding.optionC.text.toString().isEmpty()) View.GONE else View.VISIBLE
         binding.optionDButton.visibility = if(binding.optionD.text.toString().isEmpty()) View.GONE else View.VISIBLE

        countDownTimer.cancel()
        countDownTimer.start()
        progressCount = 0
        binding.progressBar.setProgress(progressCount)
        resetColor()
        enableOption()
    }



    private fun resetColor() {
        binding.optionAButton.setBackgroundResource(R.drawable.start_button_bg)
        binding.optionBButton.setBackgroundResource(R.drawable.start_button_bg)
        binding.optionCButton.setBackgroundResource(R.drawable.start_button_bg)
        binding.optionDButton.setBackgroundResource(R.drawable.start_button_bg)
    }

    private fun enableOption() {
        binding.optionAButton.isEnabled = true
        binding.optionBButton.isEnabled = true
        binding.optionCButton.isEnabled = true
        binding.optionDButton.isEnabled = true
    }

    private fun disableOption() {
        binding.optionAButton.isEnabled = false
        binding.optionBButton.isEnabled = false
        binding.optionCButton.isEnabled = false
        binding.optionDButton.isEnabled = false
    }
}