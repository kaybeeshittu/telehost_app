package ng.myflex.telehost.model

data class ResponseModel<T>(
    val status: String,
    val msg: String?,
    val data: T?
)
