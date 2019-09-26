package com.allica.ccomms.service

import java.sql.DriverManager
import scala.collection.mutable.ListBuffer
import com.typesafe.config.ConfigFactory
import org.apache.logging.log4j.scala.Logging
import com.allica.ccomms.models.RequestModels.CustomerRequest

/**
 * Data Access Layer for communicating with Hive
 *
 * @author Nilanjan Sarkar
 * @since 25 th September 2019
 */
class HiveService extends Logging {

  private val config = ConfigFactory.load()

  // Hive config
  private val hiveUrl = config.getString("ccomms.hiveService.url")
  private val hiveUserName = config.getString("ccomms.hiveService.user")
  private val hivePassword = config.getString("ccomms.hiveService.pass")
  private val hiveDriverName = config.getString("ccomms.hiveService.driverName")

  // Queries to retieve data
  private val loanReqQuery = config.getString("ccomms.batch.loanQuery")
  private val depositReqQuery = config.getString("ccomms.batch.depositQuery")

  // Load drivers
  Class.forName(hiveDriverName)

  private val con = DriverManager.getConnection(hiveUrl, hiveUserName, hivePassword)

  /**
   * @param runDate Date for which list of application numbers are to be retrieved
   */
  def getDataForLoans(runDate: String): Seq[CustomerRequest] = {
    logger.info("Fetching the list of application_numbers for loans from EDH")
    val requests = new ListBuffer[CustomerRequest]()
    val hiveStmt = con.createStatement()
    val sql = loanReqQuery.replace("?", runDate)
    val rs = hiveStmt.executeQuery(sql)
    while (rs.next()) {
      requests += CustomerRequest(rs.getString("account_number"), "Welcome_Loans")
    }
    logger.info("Successfully fetched the list of application_numbers for loans from EDH")
    rs.close()
    hiveStmt.close()
    requests
  }

  /**
   * @param runDate Date for which list of application numbers are to be retrieved
   */
  def getDataForDeposits(runDate: String): Seq[CustomerRequest] = {
    logger.info("Fetching the list of application_numbers for deposits from EDH")
    val requests = new ListBuffer[CustomerRequest]()
    val hiveStmt = con.createStatement()
    val sql = depositReqQuery.replace("?", runDate)
    val rs = hiveStmt.executeQuery(sql)
    while (rs.next()) {
      requests += CustomerRequest(rs.getString("account_number"), "Welcome_Deposits")
    }
    logger.info("Successfully fetched the list of application_numbers for deposits from EDH")
    rs.close()
    hiveStmt.close()
    requests
  }
}