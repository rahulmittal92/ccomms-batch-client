package com.allica.ccomms

import org.apache.logging.log4j.scala.Logging
import com.allica.ccomms.service.BatchService

/**
  *  The entry point to the batch client application
  *  for the bulk email facility
  *
  *  @author Nilanjan Sarkar
 */
object AllicaCcommsBatchClient extends App with Logging {

  logger.info("Launched Batch Email process for date : ")
  private val batchServ = new BatchService()
  batchServ.submitBatchJob("")
  logger.info("Batch process completed successfully!")
}
