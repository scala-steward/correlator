package com.softwaremill.correlator

import cats.data.{Kleisli, OptionT}
import com.softwaremill.correlator.CorrelationIdDecorator.CorrelationIdSource
import monix.eval.Task
import org.http4s.Request
import org.http4s.util.CaseInsensitiveString

class Http4sCorrelationMiddleware(correlationId: CorrelationIdDecorator) {

  def withCorrelationId[T, R](
      service: Kleisli[OptionT[Task, *], T, R]
  )(implicit source: CorrelationIdSource[T]): Kleisli[OptionT[Task, *], T, R] = {
    val runOptionT: T => Task[Option[R]] = service.run.andThen(_.value)
    Kleisli(correlationId.withCorrelationId[T, Option[R]](runOptionT).andThen(OptionT.apply))
  }
}

object Http4sCorrelationMiddleware {
  def apply(correlationId: CorrelationIdDecorator): Http4sCorrelationMiddleware = new Http4sCorrelationMiddleware(correlationId)

  val HeaderName: String = "X-Correlation-ID"

  implicit val source: CorrelationIdSource[Request[Task]] = (t: Request[Task]) => {
    t.headers.get(CaseInsensitiveString(HeaderName)).map(_.value)
  }
}
