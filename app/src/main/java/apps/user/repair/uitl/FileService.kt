package apps.user.repair.uitl

import apps.user.repair.model.FileDto
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileService {
    @Multipart
    @POST("/files/upload")
    fun upload(@Part file: MultipartBody.Part): Call<FileDto>
}