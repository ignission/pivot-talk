package e2e

import akka.http.scaladsl.model.{ContentTypes, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import jam.rest.routes.StaticRoute

class StaticRouteSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {

  "StaticRoute" should {
    "return an html for GET requests to the index route" in {
      Get("/") ~> StaticRoute.routes ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`text/html(UTF-8)`
      }
    }

    "return a javascript for GET requests to the js path" in {
      Get("/index.js") ~> StaticRoute.routes ~> check {
        status shouldEqual StatusCodes.OK
        mediaType shouldEqual MediaTypes.`application/javascript`
      }
    }

    "return an image for GET requests to the image path" in {
      Get("/images/bg.jpg") ~> StaticRoute.routes ~> check {
        status shouldEqual StatusCodes.OK
        mediaType shouldEqual MediaTypes.`image/jpeg`
      }
    }

    "return an html for GET requests to the any path" in {
      Get("/rooms/session-a") ~> StaticRoute.defaultRoute ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }
}