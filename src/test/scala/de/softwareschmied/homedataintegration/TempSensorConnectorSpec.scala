package de.softwareschmied.homedataintegration

import com.typesafe.scalalogging.Logger
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by Thomas Becker (thomas.becker00@gmail.com) on 2019-01-01.
  */
class TempSensorConnectorSpec extends Specification {
  val logger = Logger[TempSensorConnectorSpec]

  def tempSensorConnector = new TempSensorConnector

  "TempSensorConnector should" >> {
    "return sensor values" >> {
      val sensorsFuture = tempSensorConnector.fetchSensorData()
      val sensors = Await.result(sensorsFuture, 30 seconds)
      logger.info(s"$sensorsFuture")
      sensors.sensors.size == 2

    }
  }
}
