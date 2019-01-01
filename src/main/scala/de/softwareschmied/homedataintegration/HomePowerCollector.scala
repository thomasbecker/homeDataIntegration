package de.softwareschmied.homedataintegration

  import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.Logger
import de.softwareschmied.myhomecontrolinterface.MyHomeControlCollector
import de.softwareschmied.solarwebinterface.SolarWebConnector
import spray.json.{DefaultJsonProtocol, NullOptions}

/**
  * Created by Thomas Becker (thomas.becker00@gmail.com) on 28.11.17.
  */
case class HomePowerData(powerGrid: Double,
                         powerLoad: Double,
                         powerPv: Option[Double],
                         selfConsumption: Option[Double],
                         autonomy: Option[Double],
                         heatpumpCurrentPowerConsumption: Double,
                         heatpumpCumulativePowerConsumption: Double,
                         timestamp: Long = Instant.now.toEpochMilli)

trait HomePowerDataJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {
  implicit val homeDataFormat = jsonFormat8(HomePowerData)
}

class HomePowerCollector {
  val logger = Logger[HomePowerCollector]
  val myHomeControlCollector = new MyHomeControlCollector
  val solarWebConnector = new SolarWebConnector

  def collectData: HomePowerData = {
    val myHomeControlData = myHomeControlCollector.collectMyHomeControlPowerData()
    val powerFlowSite = solarWebConnector.getPowerFlowRealtimeData()
    val homePowerData = new HomePowerData(powerFlowSite.powerGrid, powerFlowSite.powerLoad, powerFlowSite.powerPv, powerFlowSite.selfConsumption, powerFlowSite
      .autonomy, myHomeControlData.heatpumpCurrentPowerConsumption, myHomeControlData.heatpumpCumulativePowerConsumption)
    logger.info(s"HomeData: $homePowerData")
    homePowerData
  }
}
