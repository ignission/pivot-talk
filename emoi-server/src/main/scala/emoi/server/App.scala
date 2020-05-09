package emoi.server

import akka.actor.ActorSystem
import emoi.server.dsl.{AppError, InternalError, OpenViduClientError}
import monix.eval.Task
import emoi.server.rest.Server
import akka.http.scaladsl.Http
import scala.util.control.NonFatal
import tech.ignission.openvidu4s.akka.interpreters.OpenViduHttpDSLOnAkka
import tech.ignission.openvidu4s.core.Basic
import tech.ignission.openvidu4s.core.apis.AllAPI

object App {
  import dsl.syntax._

  implicit val system = ActorSystem()
  implicit val exc    = monix.execution.Scheduler.Implicits.global

  type Result[A]     = Either[AppError, A]
  type TaskResult[A] = Task[Result[A]]

  private def startServer(
      interface: String,
      port: Int,
      openviduAPI: AllAPI[Task]
  ): TaskResult[Http.ServerBinding] =
    Task.deferFuture {
      Server.start(interface, port, openviduAPI).map(Right(_)).recover {
        case NonFatal(ex) =>
          Left(InternalError(ex): AppError)
      }
    }

  def main(args: Array[String]): Unit = {

    val interface    = Config.interface
    val port         = Config.port
    val mode         = Config.mode
    val serverConfig = Config.OpenVidu.Server
    val akkaHttpDSL  = new OpenViduHttpDSLOnAkka(debug = mode == Mode.Local || mode == Mode.Test)
    val credential   = Basic(serverConfig.username, serverConfig.password)
    val openviduAPI  = new AllAPI(serverConfig.url, credential)(akkaHttpDSL)

    val startupTask = for {
      _        <- openviduAPI.sessionAPI.getSessions.mapError(OpenViduClientError(_)).handleError
      bindings <- startServer(interface, port, openviduAPI).handleError
    } yield bindings

    startupTask.value.map {
      case Left(error) =>
        error.printStackTrace()
        system.terminate()
      case Right(_) =>
        println(s"Listening on port $port")
    }.onErrorRecover {
      case NonFatal(ex) =>
        ex.printStackTrace()
        system.terminate()
    }.runAsyncAndForget

  }
}
