package app.bangkit.ishara.data.responses.journey

import com.google.gson.annotations.SerializedName

data class JourneyResponse(

    @field:SerializedName("data")
	val data: List<JourneyItem>,

    @field:SerializedName("meta")
	val meta: Meta
)

data class Links(

	@field:SerializedName("next")
	val next: Any,

	@field:SerializedName("previous")
	val previous: Any
)

data class Pagination(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("links")
	val links: Links,

	@field:SerializedName("total_pages")
	val totalPages: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class Meta(

    @field:SerializedName("pagination")
	val pagination: Pagination,

    @field:SerializedName("success")
	val success: Boolean,

    @field:SerializedName("message")
	val message: String
)

data class JourneyLevelItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("is_stage_unlocked")
	val isStageUnlocked: Boolean?,

	@field:SerializedName("user_level_star")
	val userLevelStar: UserLevelStar?
)

data class JourneyItem(

	@field:SerializedName("is_unlocked")
	val isUnlocked: Boolean,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("levels")
	val levels: List<JourneyLevelItem>
)

data class UserLevelStar(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("obtained_stars")
	val obtainedStars: Int?
)
