package controllers

import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  import play.api.Play.current
  import play.api.mvc._

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    MyWebSocketActor.props(out)
  }

  import akka.actor._

  object MyWebSocketActor {
    def props(out: ActorRef) = Props(new MyWebSocketActor(out, 50))
  }

  class MyWebSocketActor(out: ActorRef, rowSize : Int) extends Actor {
    val boardSize = rowSize * rowSize
    var sotSub : Cancellable = null
    var sot : String = null

    override def postStop = cancelUpdates

    def receive = {
      case "sot" => {
        println("Requested the state of the world")

        sot = getStateOfTheWorld
        out ! sot

        cancelUpdates

        import scala.concurrent.duration._
        sotSub = context.system.scheduler.schedule(0 millis, 2 second, self, "shift")
      }
      case "shift" => {
        out ! shiftStateOfTheWorld
      }
      case "end" => {
        self ! PoisonPill
      }
      case _ => {
        println("Unhandled message. Killing ws.")
        out ! "Unhandled message. Killing ws."
        self ! PoisonPill
      }
    }

    def cancelUpdates = if(sotSub != null && !sotSub.isCancelled) sotSub.cancel()

    def getStateOfTheWorld: String = {
      val sb = new StringBuilder(boardSize).append("0"*boardSize)
      sb.setCharAt(0, '1')
      sb.toString()
    }

    def shiftStateOfTheWorld: String = {
      val sb = new StringBuilder(sot)
      val i = sot.indexOf("1")
      sb.setCharAt(i, '0')
      sb.setCharAt((i + 1) % boardSize, '1')
      sot = sb.toString()
      sot
    }
  }

}