package com.bipulhstu.programminghero.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bipulhstu.programminghero.R
import com.bipulhstu.programminghero.databinding.ActivityQuestionsBinding
import com.bipulhstu.programminghero.model.Question
import com.bipulhstu.programminghero.utils.SharedPreferenceInfo
import com.bipulhstu.programminghero.viewmodel.QuestionViewModel
import com.squareup.picasso.Picasso


class QuestionsActivity : AppCompatActivity() {
    private lateinit var preferenceInfo: SharedPreferenceInfo
    private lateinit var binding: ActivityQuestionsBinding
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var progressDialog: ProgressDialog
    lateinit var questionList: List<Question>
    lateinit var questionViewModel: QuestionViewModel
    private lateinit var question: Question
    var currentQuestion = 0
    var progressCount = 0
    var gainedScore = 0
    var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionViewModel = ViewModelProvider(this).get(QuestionViewModel::class.java)
        preferenceInfo = SharedPreferenceInfo(this)
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)


        progressDialog.show()
        progressDialog.setContentView(R.layout.custom_loading_dialog)
        progressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)


        getQuestionList()

        initializeCountDownTimer()

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
            gainedScore += question.score
            binding.score.text = "Score: $gainedScore"
        } else {
            binding.optionAButton.setBackgroundResource(if ("A" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionBButton.setBackgroundResource(if ("B" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionCButton.setBackgroundResource(if ("C" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            binding.optionDButton.setBackgroundResource(if ("D" == question.correctAnswer) R.drawable.right_answer_bg else R.drawable.start_button_bg)
            selectedOption.setBackgroundResource(R.drawable.wrong_answer_bg)
        }
        Handler().postDelayed({
            getNextQuestion()
        }, 2000)
    }


    private fun getQuestionList() {
        questionViewModel.getQuestions()?.observe(this) {

            fun onChanged(@Nullable questions: List<Question>) {
                questionList = questions
                //shuffle(questionList)
                currentQuestion = index + 1
                binding.progressBar.visibility = View.VISIBLE
                setQuestion(questionList[index])

                progressDialog.dismiss()
            }
            onChanged(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(question: Question) {
        this.question = question
        binding.questionCount.text = "Question: $currentQuestion/" + questionList.size
        binding.score.text = "Score: $gainedScore"
        binding.questionPoint.text = question.score.toString() + " Point"
        binding.mainQuestion.text = question.question

        binding.questionImage.visibility = if (question.questionImageUrl == null) View.INVISIBLE else View.VISIBLE
        Picasso.with(this).load(question.questionImageUrl).into(binding.questionImage)

        binding.optionA.text = question.answers.A
        binding.optionB.text = question.answers.B
        binding.optionC.text = question.answers.C
        binding.optionD.text = question.answers.D

        binding.optionAButton.visibility =
            if (binding.optionA.text.toString().isEmpty()) View.GONE else View.VISIBLE
        binding.optionBButton.visibility =
            if (binding.optionB.text.toString().isEmpty()) View.GONE else View.VISIBLE
        binding.optionCButton.visibility =
            if (binding.optionC.text.toString().isEmpty()) View.GONE else View.VISIBLE
        binding.optionDButton.visibility =
            if (binding.optionD.text.toString().isEmpty()) View.GONE else View.VISIBLE

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

    private fun initializeCountDownTimer() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressCount++
                //binding.progressBar.setProgress(progressCount)
            }

            override fun onFinish() {
                //Toast.makeText(this@QuestionsActivity, "Working", Toast.LENGTH_SHORT).show()
                getNextQuestion()
            }
        }
    }

    private fun getNextQuestion() {
        index++
        currentQuestion = index + 1
        if (index < questionList.size) setQuestion(questionList[index])
        else {
            val previousHighScore = preferenceInfo.getPoint("high_score")
            val highScore =
                if (preferenceInfo.getPoint("high_score") <= gainedScore) gainedScore else previousHighScore
            preferenceInfo.storePoint(highScore, "high_score")
            finish()
        }
    }

}