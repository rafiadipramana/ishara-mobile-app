package app.bangkit.ishara.data.responses.login.error

import com.google.gson.annotations.SerializedName

data class LoginErrorResponse(

	@field:SerializedName("meta")
	val meta: Meta
)

data class Meta(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("error")
	val error: String
)
