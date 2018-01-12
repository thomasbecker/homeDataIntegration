package de.softwareschmied.homedataintegration

import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.Logger
import de.softwareschmied.myhomecontrolinterface.{MyHomeControlCollector, MyHomeControlData}
import de.softwareschmied.solarwebinterface.{PowerFlowSite, SolarWebConnector}
import play.api.libs.json.{Format, Json}
import spray.json.{DefaultJsonProtocol, NullOptions}

/**
 * Created by Thomas Becker (thomas.becker00@gmail.com) on 28.11.17.
 */
case class HomeData(powerGrid: Double, powerLoad: Double, powerPv: Option[Double], selfConsumption: Option[Double],
                    autonomy: Option[Double], heatpumpPowerConsumption: Double, livingRoomTemp: Double, sleepingRoomCo2: Double,
                    timestamp: Long = Instant.now.toEpochMilli)

trait HomeDataJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {
  implicit val homeDataFormat = jsonFormat9(HomeData)
}

class HomeCollector {
  val logger = Logger[HomeCollector]
  val myHomeControlCollector = new MyHomeControlCollector
  val solarWebConnector = new SolarWebConnector

  def collectData: HomeData = {
    val myHomeControlData = myHomeControlCollector.collectMyHomeControlData()
    val powerFlowSite = solarWebConnector.getPowerFlowRealtimeData()
    val homeData = new HomeData(powerFlowSite.powerGrid, powerFlowSite.powerLoad, powerFlowSite.powerPv, powerFlowSite.selfConsumption, powerFlowSite
      .autonomy, myHomeControlData.heatpumpPowerConsumption, myHomeControlData.livingRoomTemp, myHomeControlData.sleepingRoomCo2)
    logger.info(s"HomeData: $homeData")
    homeData
  }
}
