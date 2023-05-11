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

package com.fortyseven.core.codecs.app

import com.fortyseven.coreheaders.codecs.app.AppTypes
import com.fortyseven.coreheaders.model.app.types.Meters
import vulcan.Codec

object AppTypes extends AppTypes[Codec]:

  given metersCodec: Codec[Meters] = ???
