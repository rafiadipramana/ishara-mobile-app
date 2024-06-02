package app.bangkit.ishara.data.responses.register

import app.bangkit.ishara.data.responses.login.Data
import app.bangkit.ishara.data.responses.login.Meta
import app.bangkit.ishara.data.responses.login.Pagination
import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("meta")
	val meta: Meta
)

data class Data(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String
)

data class Pagination(
	val any: Any? = null
)

data class Meta(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
