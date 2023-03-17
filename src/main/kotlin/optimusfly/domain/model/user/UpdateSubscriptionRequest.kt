package optimusfly.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateSubscriptionRequest(val newSubscription: Int)
