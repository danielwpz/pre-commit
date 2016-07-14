import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

/**
 * Created by Daniel on 24-Nov-2015.
 */
object main {
  def main(args: Array[String]) {
    var x = 19

    val Authorize = authorized {
      x match {
        case 1 => None
        case _ =>
          Some(x)
      }
    } _

    val result = Authorize { y =>
      Future.successful("x is " + y)
    }

    result.foreach(println)

    while (true) { x += 1 }
  }

  private def goBack() = {
    Future("Back")
  }

  implicit val doFailure: () => Future[String] = { () =>
    Future("Back")
  }

  def authorized[T](getCredential: => Option[T])(onSuccess: T => Future[String])(implicit onFailure: () => Future[String]): Future[String] = {
    getCredential match {
      case Some(x) => onSuccess(x)
      case None => onFailure()
    }
  }
}
