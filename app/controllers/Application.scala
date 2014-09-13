package controllers

import play.api._
import play.api.libs.iteratee.{Iteratee, Concurrent}
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  import play.api.mvc._
  import play.api.Play.current

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }

  import akka.actor._

  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out))
  }

  class MyWebSocketActor(out: ActorRef) extends Actor {
    def receive = {
      case "sot" => {
        println("Requested the state of the world")

        //TODO
        out ! getStateOfTheWorld
      }
      case _ => {
        println("Unhandled message. Killing ws.")
        out ! "Unhandled message. Killing ws."
        self ! PoisonPill
      }
    }

    def getStateOfTheWorld : String = {
      "0" * 2500
    }
  }

}