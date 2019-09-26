package com.allica.ccomms.service

import scala.collection.mutable.ListBuffer
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.entity.StringEntity
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write
import com.allica.ccomms.models.RequestModels.BatchCustomerRequest
import com.allica.ccomms.models.RequestModels.CustomerRequest
import com.typesafe.config.ConfigFactory
import org.apache.logging.log4j.scala.Logging

/**
 * Submits a batch email request for both Loans and Deposit
 *
 * @author Nilanjan Sarkar
 *
 */
class BatchService extends Logging {

  private val config = ConfigFactory.load()

  private val batchServiceUrl = config.getString("ccomms.batchService.url")
  private val connectTimeout = 100000
  private val connectionRequestTimeout = 1000000
  private val socketTimeout = -1

  private implicit val formats = DefaultFormats

  /**
    * Fetches the list of application_numbers for both
    * loans and deposits for the particular run date from EDH,
    * forms the request and submits it as HTTP POST to Allica Customer Communication Server
    *
    *@param runDate Date of run for the batch email process
    */
  def submitBatchJob(runDate: String) {
    val requests = new ListBuffer[CustomerRequest]()
    val hs = new HiveService
    val loanReqs = hs.getDataForLoans("12082019")
    val depositReqs = hs.getDataForDeposits("2021-03-18 00:00:00.0")
    logger.info("application_numbers retrieved successfully for both loans and deposits")
    requests ++= loanReqs
    requests ++= depositReqs
    val batchReq = BatchCustomerRequest(requests)
    val jsonReq = write(batchReq)
    logger.info("Request formed successfully")
    doPost(jsonReq)
  }

  /**
    * Does an HTTP POST to Allica Customer Communication Server
    *
    * @param request The batch email request in JSON
    */
  private def doPost(request: String) {
    
    val config = RequestConfig.custom()
    .setConnectTimeout(connectTimeout)
    .setConnectionRequestTimeout(connectionRequestTimeout)
    .setSocketTimeout(socketTimeout)
    .build()
    
    val httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    //Define a postRequest request
    val postRequest = new HttpPost(batchServiceUrl)
    postRequest.setHeader("Accept", "application/json")
    //Set the API media type in http content-type header
    postRequest.setHeader("Content-type", "application/json")
    val userEntity = new StringEntity(request)
    postRequest.setEntity(userEntity)
    logger.info("Posting the batch request to Allica Customer communication Server for sending email")
    val response = httpClient.execute(postRequest)
    //verify the valid error code first
    val statusCode = response.getStatusLine().getStatusCode();
    if (statusCode != 200) {
      throw new RuntimeException("Failed with HTTP error code : " + statusCode);
    }
    httpClient.close()
  }
}