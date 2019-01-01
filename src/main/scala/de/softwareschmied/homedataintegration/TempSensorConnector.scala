package de.softwareschmied.homedataintegration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, ResponseEntity, Uri}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.Logger
import de.softwareschmied.homedataintegration.JsonSupport.JsonSupport

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Created by Thomas Becker (thomas.becker00@gmail.com) on 2019-01-01.
  */
case class Sensor(id: String, value: String)

case class SensorValues(sensors: Seq[Sensor])

class TempSensorConnector extends JsonSupport {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val logger = Logger[TempSensorConnector]

  val httpClient = Http().outgoingConnection(host = "black-pearl", port = 8080)

  def fetchSensorData(): Future[SensorValues] = {
    Source.single(
      HttpRequest(
        method = HttpMethods.GET,
        uri = Uri(s"""/sensors"""))
    )
      .via(httpClient)
      .mapAsync(1)(response => Unmarshal(response.entity).to[SensorValues])
      .runWith(Sink.head)
  }
}
