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

  if(args.length < 1 ) {
    throw  new IllegalArgumentException("Insufficient number of arguments. Usage : <runDate>")
  }
  val runDate = args(0)
  logger.info(s"Launched Batch Email process for date : ${runDate}")
  private val batchServ = new BatchService()
  batchServ.submitBatchJob(runDate)
  logger.info("Batch process completed successfully!")
}
