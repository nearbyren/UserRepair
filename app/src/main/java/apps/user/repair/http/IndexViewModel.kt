package apps.user.repair.http

import androidx.lifecycle.MutableLiveData
import apps.user.repair.model.ConfirmDto
import apps.user.repair.model.EditPasswordDto
import apps.user.repair.model.EnrollDto
import apps.user.repair.model.FileSuccessDto
import apps.user.repair.model.InventoryDto
import apps.user.repair.model.LoginDto
import apps.user.repair.model.LogoutDto
import apps.user.repair.model.NoteDto
import apps.user.repair.model.ServiceDto
import apps.user.repair.model.SubmitDto
import kotlinx.coroutines.delay
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.vm.BaseAppViewModel
import java.io.File

/**
 * @author: lr
 * @created on: 2023/12/18 9:14 PM
 * @description:
 */
class IndexViewModel : BaseAppViewModel() {

    private val repo by lazy { RepoImpl() }

    var serviceDtos = MutableLiveData<MutableList<ServiceDto>>()

    var service = MutableLiveData<ServiceDto>()

    var inventoryDto = MutableLiveData<InventoryDto>()

    var noteDto = MutableLiveData<NoteDto>()

    var confirmDto = MutableLiveData<ConfirmDto>()

    val login = MutableLiveData<LoginDto>()

    val enrollDto = MutableLiveData<EnrollDto>()

    val editPasswordDto = MutableLiveData<EditPasswordDto>()

    val submit = MutableLiveData<SubmitDto>()

    val start = MutableLiveData<Boolean>()

    val logoutDto = MutableLiveData<LogoutDto>()

    val fileSuccess = MutableLiveData<FileSuccessDto>()

    fun numbers(numbers: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["numbers"] = numbers
            repo.numbers(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    inventoryDto.value = it
                }.onFailure { _, _ ->
                    serviceDtos.value = mutableListOf()
                }.onCatch {
                    serviceDtos.value = mutableListOf()
                }
        }
    }

    fun inventoryId(id: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.inventoryId(params, id).onCompletion { showLoadingView(false) }
                .onSuccess {
                    serviceDtos.value = it
                }.onFailure { _, msg ->
                    msg?.let {
                        ToastUtils.showToast(msg)
                    }
                }.onCatch {
                    ToastUtils.showToast("服務器異常")
                }
        }
    }

    fun note(memberName: String, name: String, phone: String, describes: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["memberName"] = memberName
            params["name"] = name
            params["phone"] = phone
            params["describes"] = describes
            repo.note(params)
                .onCompletion {
                    showLoadingView(false)
                }
                .onSuccess {
                noteDto.value = NoteDto()
            }.onFailure { _, msg ->
                noteDto.value = msg?.let { NoteDto(it) }
            }.onCatch {
                noteDto.value = NoteDto("服務器異常")
            }
        }
    }

    fun confirm(numbers: String, id: Int) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["numbers"] = numbers
            params["state"] = 2
            params["id"] = id
            repo.confirm(params)
                .onCompletion {
                    showLoadingView(false)
                }
                .onSuccess {
                    confirmDto.value = ConfirmDto()
                }.onFailure { _, msg ->
                    msg?.let { msgs ->
                        confirmDto.value = ConfirmDto(msg = msgs)
                    }
                }.onCatch {
                    confirmDto.value = ConfirmDto(msg = "服務器異常")
                }
        }
    }

    fun mailId(id: Int) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.mailId(params, id).onSuccess {

            }.onFailure { _, _ ->
            }.onCatch {
            }
        }
    }

    fun updateEmail() {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.updateEmail(params).onSuccess {

            }.onFailure { _, _ ->

            }.onCatch {

            }
        }
    }

    fun revisePassword(id: String, password: String, newPassword: String) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["id"] = id
            //原密码
            params["password"] = password
            //新密码
            params["newPassword"] = newPassword
            repo.revisePassword(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    editPasswordDto.value = EditPasswordDto()
                }.onFailure { _, msg ->
                    editPasswordDto.value = msg?.let { EditPasswordDto(msg = it) }
                }.onCatch {
                    editPasswordDto.value = EditPasswordDto(msg = "服務器異常")
                }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["email"] = email
            params["password"] = password
            repo.login(params)
                .onCompletion {
                    showLoadingView(false)
                }
                .onSuccess {
                    login.value = it
                }.onFailure { _, message ->
                    login.value = LoginDto(message = message)
                }.onCatch {
                    login.value = LoginDto(message = "服務器異常")
                }
        }
    }

    fun logout() {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            repo.logout(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    logoutDto.value = LogoutDto()
                }.onFailure { _, message ->
                    logoutDto.value = message?.let { LogoutDto(msg = it) }
                }.onCatch {
                    logoutDto.value = LogoutDto(msg = "服務器異常")
                }
        }
    }

    fun upload(file: File) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["file"] = file
            repo.upload(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    fileSuccess.value = FileSuccessDto(path = it)
                }.onFailure { _, message ->
                    fileSuccess.value = FileSuccessDto(msg = message)
                }.onCatch {
                    fileSuccess.value = FileSuccessDto(msg = it.errorMsg)
                }
        }
    }

    fun enroll(
        email: String,
        password: String,
        memberName: String,
        mane: String,
        phone: String,
        schoolImage: String
    ) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["email"] = email
            params["password"] = password
            params["memberName"] = memberName
            params["mane"] = mane
            params["phone"] = phone
            params["schoolImage"] = schoolImage
            repo.enroll(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    enrollDto.value = EnrollDto()
                }.onFailure { _, message ->
                    enrollDto.value = EnrollDto(msg = message)
                }.onCatch {
                    enrollDto.value = EnrollDto(msg = "服務器異常")
                }
        }
    }

    fun submit(
        repairAddress: String,
        describes: String,
        addressImage: String,
        locationImage: String,
        urgent: Int,
        urgentTime: String?,
        type: Int,
        tMemberId: Int
    ) {
        launchOnUI {
            showLoadingView(true)
            val params = mutableMapOf<String, Any>()
            params["repairAddress"] = repairAddress
            params["describes"] = describes
            params["addressImage"] = addressImage
            params["locationImage"] = locationImage
            params["urgent"] = urgent
            if (urgent == 1) {
                params["urgentTime"] = urgentTime!!
            }
            params["type"] = type
            params["tmemberId"] = tMemberId
            repo.submit(params)
                .onCompletion { showLoadingView(false) }
                .onSuccess {
                    submit.value = it
                }.onFailure { _, message ->
                    submit.value = SubmitDto(msg = message)
                }.onCatch {
                    submit.value = SubmitDto(msg = "服務器異常")
                }
        }
    }

    fun start() {
        launchOnUI {
            delay(500)
            start.value = true

        }
    }
}