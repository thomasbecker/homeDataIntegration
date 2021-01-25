package de.softwareschmied.homedataintegration

import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.Logger
import de.softwareschmied.myhomecontrolinterface.MyHomeControlCollector
import spray.json.{DefaultJsonProtocol, NullOptions}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
 * Created by Thomas Becker (thomas.becker00@gmail.com) on 28.11.17.
 */
case class HomeEnvironmentData(officeTemp: Double,
                               livingRoomCo2: Double,
                               livingRoomTemp: Double,
                               livingRoomHumidity: Double,
                               sleepingRoomCo2: Double,
                               sleepingRoomTemp: Double,
                               sleepingRoomHumidity: Double,
                               basementTemp: Double,
                               basementHumidity: Double,
                               heatingLeading: Double,
                               heatingInlet: Double,
                               waterTankMiddle: Double,
                               waterTankBottom: Double,
                               utilityRoomTemp: Double,
                               utilityRoomHumidity: Double,
                               timestamp: Long = Instant.now.toEpochMilli)

trait HomeEnvironmentDataJsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {
  implicit val homeEnvironmentDataFormat = jsonFormat16(HomeEnvironmentData)
}

class HomeEnvironmentCollector {
  val logger = Logger[HomePowerCollector]
  val myHomeControlCollector = new MyHomeControlCollector
  val tempSensorConnector = new TempSensorConnector

  def collectData: HomeEnvironmentData = {
    val myHomeControlData = myHomeControlCollector.collectMyHomeControlEnvironmentData()
    val tempSensors = Await.result(tempSensorConnector.fetchSensorData(), 30 seconds)
    val homeEnvironmentData = HomeEnvironmentData(myHomeControlData.officeTemp, myHomeControlData.livingRoomCo2, myHomeControlData.livingRoomTemp,
      myHomeControlData.livingRoomHumidity, myHomeControlData.sleepingRoomCo2, myHomeControlData.sleepingRoomTemp, myHomeControlData.sleepingRoomHumidity,
      myHomeControlData.basementTemp, myHomeControlData.basementHumidity, tempSensors.sensors(1).value.toDouble,
      tempSensors.sensors(5).value.toDouble,
      tempSensors.sensors.head.value.toDouble, tempSensors.sensors(4).value.toDouble, tempSensors.sensors(2).value
        .toDouble, tempSensors.sensors(3).value.toDouble)
    logger.info(s"HomeData: $homeEnvironmentData")
    homeEnvironmentData
  }
}
