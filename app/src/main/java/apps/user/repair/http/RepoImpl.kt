package apps.user.repair.http

import apps.user.repair.model.CenterDto
import apps.user.repair.model.EditPasswordDto
import apps.user.repair.model.EnrollDto
import apps.user.repair.model.InventoryDto
import apps.user.repair.model.LoginDto
import apps.user.repair.model.LogoutDto
import apps.user.repair.model.NoteDto
import apps.user.repair.model.ServiceDto
import apps.user.repair.model.SubmitDto
import apps.user.repair.uitl.ApiHttpUrl
import com.google.gson.reflect.TypeToken
import nearby.lib.netwrok.response.CorHttp
import nearby.lib.netwrok.response.InfoResponse
import nearby.lib.netwrok.response.ResponseHolder

class RepoImpl : Repo {

    override suspend fun numbers(
        params: MutableMap<String, Any>
    ): ResponseHolder<InventoryDto> {
        return CorHttp.getInstance().get(
            url = ApiHttpUrl.numbers,
            params = params,
            type = object : TypeToken<InfoResponse<InventoryDto>>() {}.type
        )
    }

    override suspend fun inventoryId(
        params: MutableMap<String, Any>,
        id: String
    ): ResponseHolder<MutableList<ServiceDto>> {
        return CorHttp.getInstance().get(
            url = "${ApiHttpUrl.inventory_id}${id}",
            params = params,
            type = object : TypeToken<InfoResponse<MutableList<ServiceDto>>>() {}.type
        )
    }

    override suspend fun note(params: MutableMap<String, Any>): ResponseHolder<NoteDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.note,
            params = params,
            type = object : TypeToken<InfoResponse<NoteDto>>() {}.type
        )
    }

    override suspend fun confirm(params: MutableMap<String, Any>): ResponseHolder<CenterDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.confirm,
            params = params,
            type = object : TypeToken<InfoResponse<CenterDto>>() {}.type
        )
    }

    override suspend fun mailId(
        params: MutableMap<String, Any>,
        id: Int
    ): ResponseHolder<ServiceDto> {
        return CorHttp.getInstance().post(
            url = "${ApiHttpUrl.mail_id}${id}",
            params = params,
            type = object : TypeToken<InfoResponse<ServiceDto>>() {}.type
        )
    }

    override suspend fun updateEmail(params: MutableMap<String, Any>): ResponseHolder<ServiceDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.updateEmail,
            params = params,
            type = object : TypeToken<InfoResponse<ServiceDto>>() {}.type
        )
    }

    override suspend fun revisePassword(
        params: MutableMap<String, Any>

    ): ResponseHolder<EditPasswordDto> {
        return CorHttp.getInstance().post(
            url = "${ApiHttpUrl.revisePassword}",
            params = params,
            type = object : TypeToken<InfoResponse<EditPasswordDto>>() {}.type
        )
    }

    override suspend fun login(params: MutableMap<String, Any>): ResponseHolder<LoginDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.login,
            params = params,
            type = object : TypeToken<InfoResponse<LoginDto>>() {}.type
        )
    }

    override suspend fun enroll(params: MutableMap<String, Any>): ResponseHolder<EnrollDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.enroll,
            params = params,
            type = object : TypeToken<InfoResponse<EnrollDto>>() {}.type
        )
    }


    override suspend fun submit(params: MutableMap<String, Any>): ResponseHolder<SubmitDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl. submit,
            params = params,
            type = object : TypeToken<InfoResponse<SubmitDto>>() {}.type
        )
    }

    override suspend fun logout(params: MutableMap<String, Any>): ResponseHolder<LogoutDto> {
        return CorHttp.getInstance().post(
            url = ApiHttpUrl.logout,
            params = params,
            type = object : TypeToken<InfoResponse<LogoutDto>>() {}.type
        )
    }

    override suspend fun upload(
        params: MutableMap<String, Any>
    ): ResponseHolder<String> {
        return CorHttp.getInstance().postMultipart(
            url = ApiHttpUrl.upload,
            headers = null,
            params = params,
            type = object : TypeToken<InfoResponse<String>>() {}.type
        )
    }
}