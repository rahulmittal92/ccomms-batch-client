package com.allica.ccomms.models

object RequestModels {

  case class CustomerRequest(id: String, template: String)
  case class BatchCustomerRequest(customerRequests: Seq[CustomerRequest])
}