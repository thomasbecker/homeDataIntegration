package de.softwareschmied.homedataintegration.JsonSupport

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.softwareschmied.homedataintegration.{Sensor, SensorValues}
import spray.json.DefaultJsonProtocol.{jsonFormat1, jsonFormat2}
import spray.json.{DefaultJsonProtocol, NullOptions}

/**
  * Created by Thomas Becker (thomas.becker00@gmail.com) on 2019-01-01.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with NullOptions {
  implicit val sensorFormat = jsonFormat2(Sensor)
  implicit val sensorValuesFormat = jsonFormat1(SensorValues)
}
