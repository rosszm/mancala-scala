package dev.zacharyross.mancala.server

import _root_.io.circe._
import _root_.org.http4s.ember.server.EmberServerBuilder
import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import fs2._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.websocket.WebSocketBuilder2
import org.http4s.websocket.WebSocketFrame

import scala.concurrent.duration._


object Server extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    val host = host"0.0.0.0"
    val port = port"8080"
    for {
      server <- EmberServerBuilder
        .default[IO]
        .withHost(host)
        .withPort(port)
        .withHttpWebSocketApp(service[IO])
        .build
    } yield server
  }.use(server =>
    IO.delay(println(s"Server Has Started at ${server.address}")) >>
      IO.never.as(ExitCode.Success)
  )

  def service[F[_]: Async](wsb: WebSocketBuilder2[F]): HttpApp[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      // Create player with randomly generated id.
      case req @ POST -> Root / "player" =>
        val json = req.decodeJson[Json]
        for {
          json <- req.decodeJson[Json]
          resp <- Ok(json)
        } yield resp

      // Create game with the specified
      case GET -> Root =>
        Ok(Json.obj("root" -> Json.fromString("GET")))

      case GET -> Root / "hello" / name =>
        Ok(show"Hi $name!")

      case GET -> Root / "ws" =>
        val send: Stream[F, WebSocketFrame] =
          Stream.awakeEvery[F](1.seconds).map(_ => WebSocketFrame.Text("text"))

        val receive: Pipe[F, WebSocketFrame, Unit] = _.evalMap {
          case WebSocketFrame.Text(text, _) => Sync[F].delay(println(text))
          case other => Sync[F].delay(println(other))
        }
        wsb.build(send, receive)
    }
    .orNotFound
  }
}


case class Player(name: String)