package com.example.triviastarter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.triviastarter.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    data class Question(
        val text: String,
        val answers: List<String>,
        var currentAnswers: Int,
    )

    private val questions: MutableList<Question> = mutableListOf(
        Question(
            text = "What is Android Jetpack?",
            answers = listOf("all of these", "tools", "documentation", "libraries"),
            1
        ),
        Question(
            text = "Base class for Layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot"),
            1
        ),
        Question(
            text = "Layout for complex Screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout"),
            1
        ),
        Question(
            text = "Pushing structured data into a Layout?",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick"),
            1
        ),
        Question(
            text = "Inflate layout in fragments?",
            answers = listOf("onCreateView", "onViewCreated", "onCreateLayout", "onInflateLayout"),
            1
        ),
        Question(
            text = "Build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle"),
            1
        ),
        Question(
            text = "Android vector format?",
            answers = listOf(
                "VectorDrawable",
                "AndroidVectorDrawable",
                "DrawableVector",
                "AndroidVector"
            ),
            1
        ),
        Question(
            text = "Android Navigation Component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher"),
            1
        ),
        Question(
            text = "Registers app with launcher?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher"),
            1
        ),
        Question(
            text = "Mark a layout for Data Binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"),
            1
        )
    )

    lateinit var binding: FragmentGameBinding
    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = ((questions.size + 1) / 2).coerceAtMost(3)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        randomizeQuestion()

        binding.game = this

        binding.submitButton.setOnClickListener {
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            if (-1 != checkedId) {
                var answersIndex = 0
                when (checkedId) {
                    R.id.firstAnswerRadioButton -> answersIndex = 1
                    R.id.secondAnswerRadioButton -> answersIndex = 2
                    R.id.thirdAnswerRadioButton -> answersIndex = 3
                }
                if (answers[answersIndex] != currentQuestion.answers[0]) {
                    questionIndex++

                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        requireView().findNavController()
                            .navigate(GameFragmentDirections.actionGameFragmentToGameWonFragment(
                                numQuestions,
                                questionIndex))
                    }
                } else {
                    requireView().findNavController()
                        .navigate(GameFragmentDirections.actionGameFragmentToGameOverFragment())
                }

            }
        }

        return binding.root
    }

    private fun randomizeQuestion() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        answers = currentQuestion.answers.toMutableList()
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }
}