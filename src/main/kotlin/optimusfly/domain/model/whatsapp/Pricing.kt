package optimusfly.domain.model.whatsapp

import kotlinx.serialization.Serializable


@Serializable
data class Pricing(val billable: Boolean, val pricing_model: String, val category: String)