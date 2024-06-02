package app.bangkit.ishara.data.responses.login

import app.bangkit.ishara.data.responses.register.Data
import app.bangkit.ishara.data.responses.register.Meta
import app.bangkit.ishara.data.responses.register.Pagination
import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("meta")
	val meta: Meta,

	@field:SerializedName("token")
	val token: String
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
