/*
 * Copyright 2023 Xebia Functional
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortyseven.domain.model.iot

import scala.util.control.NoStackTrace

/** Contains possible errors for the application domain.
  */
object errors:

  /** Represents the OutOfBoundsError with a cause in the message.
    *
    * Extends [[java.lang.RuntimeException]].
    *
    * Extends [[scala.util.control.NoStackTrace]].
    *
    * @constructor
    *   Creates an OutOfBoundsError with a specified `message`.
    * @param message
    *   Reason for the OutOfBoundsError.
    */
  final case class OutOfBoundsError(message: String) extends RuntimeException(message) with NoStackTrace

end errors
