package app.bangkit.ishara.data.responses.journey.level

import com.google.gson.annotations.SerializedName

data class LevelResponse(

	@field:SerializedName("data")
	val data: List<LevelItem?>? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class LevelItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
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
