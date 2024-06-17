package app.bangkit.ishara.data.responses.auth

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Data(

	@field:SerializedName("token")
	val token: String? = null
)

data class Meta(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
