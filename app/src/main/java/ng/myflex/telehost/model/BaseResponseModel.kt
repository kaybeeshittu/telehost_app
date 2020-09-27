package ng.myflex.telehost.model

data class BaseResponseModel<T, K>(
    val status: String,
    val msg: K?,
    val data: T?
)
