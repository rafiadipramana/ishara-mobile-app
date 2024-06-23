package app.bangkit.ishara.data.responses.journey.question

import com.google.gson.annotations.SerializedName

data class QuestionResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Meta(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pagination(
	val any: Any? = null
)

data class AnswersItem(

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_correct")
	val isCorrect: Boolean? = null
)

data class DataItem(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("correct_answer")
	val correctAnswer: String? = null,

	@field:SerializedName("answers")
	val answers: List<AnswersItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
