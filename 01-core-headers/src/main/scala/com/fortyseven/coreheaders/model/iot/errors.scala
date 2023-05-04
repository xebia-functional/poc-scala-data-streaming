package com.fortyseven.coreheaders.model.iot

import scala.util.control.NoStackTrace

object errors:

  final case class OutOfBoundsError(msg: String) extends RuntimeException(msg) with NoStackTrace
