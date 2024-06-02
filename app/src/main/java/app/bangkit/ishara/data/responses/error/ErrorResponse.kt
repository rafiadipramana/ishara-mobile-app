package app.bangkit.ishara.data.responses.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("meta")
	val meta: Meta
)

data class Meta(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("error")
	val error: String
)
