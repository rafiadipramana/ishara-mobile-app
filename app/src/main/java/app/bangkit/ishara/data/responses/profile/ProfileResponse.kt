package app.bangkit.ishara.data.responses.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

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

data class Data(

	@field:SerializedName("role")
	val role: String? = null,

	@field:SerializedName("total_stars")
	val totalStars: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Pagination(
	val any: Any? = null
)
