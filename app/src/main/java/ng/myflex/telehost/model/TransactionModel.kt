package ng.myflex.telehost.model

data class TransactionModel(
    val id: Double,
    val user_id: Double,
    val transaction_type: String,
    val amount: String,
    val status: Int,
    val created_at: String,
    val updated_at: String
)