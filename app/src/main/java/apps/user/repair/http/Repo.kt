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
import nearby.lib.netwrok.response.ResponseHolder

interface Repo {
    suspend fun inventory(params: MutableMap<String, Any>): ResponseHolder<MutableList<ServiceDto>>
    suspend fun inventoryId(params: MutableMap<String, Any>, id: Int): ResponseHolder<InventoryDto>
    suspend fun note(params: MutableMap<String, Any>): ResponseHolder<NoteDto>
    suspend fun confirm(params: MutableMap<String, Any>): ResponseHolder<CenterDto>
    suspend fun mailId(params: MutableMap<String, Any>, id: Int): ResponseHolder<ServiceDto>
    suspend fun updateEmail(params: MutableMap<String, Any>): ResponseHolder<ServiceDto>
    suspend fun revisePasswordId(params: MutableMap<String, Any>, id: Int): ResponseHolder<EditPasswordDto>
    suspend fun login(params: MutableMap<String, Any>): ResponseHolder<LoginDto>
    suspend fun enroll(params: MutableMap<String, Any>): ResponseHolder<EnrollDto>
    suspend fun submit(params: MutableMap<String, Any>): ResponseHolder<SubmitDto>
    suspend fun logout(params: MutableMap<String, Any>): ResponseHolder<LogoutDto>


}