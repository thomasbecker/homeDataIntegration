package de.softwareschmied.homedataintegration

import java.time.Instant

import com.typesafe.scalalogging.Logger
import de.softwareschmied.myhomecontrolinterface.{MyHomeControlCollector, MyHomeControlData}
import de.softwareschmied.solarwebinterface.{PowerFlowSite, SolarWebConnector}

/**
 * Created by Thomas Becker (thomas.becker00@gmail.com) on 28.11.17.
 */
case class HomeData(powerFlowSite: PowerFlowSite, myHomeControlData: MyHomeControlData, timestamp: Long = Instant.now.getEpochSecond)

class HomeCollector {
  val logger = Logger[HomeCollector]
  val myHomeControlCollector = new MyHomeControlCollector
  val solarWebConnector = new SolarWebConnector

  def collectData: HomeData = {
    val myHomeControlData = myHomeControlCollector.collectMyHomeControlData()
    val powerFlowSite = solarWebConnector.getPowerFlowRealtimeData()
    val homeData = new HomeData(powerFlowSite, myHomeControlData)
    logger.info(s"HomeData: $homeData")
    homeData
  }
}
