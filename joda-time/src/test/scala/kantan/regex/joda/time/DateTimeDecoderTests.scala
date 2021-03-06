/*
 * Copyright 2016 Nicolas Rinaudo
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

package kantan.regex
package joda.time

import laws.discipline._, arbitrary._
import org.joda.time.DateTime

class DateTimeDecoderTests extends DisciplineSuite {

  checkAll("GroupDecoder[DateTime]", GroupDecoderTests[DateTime].decoder[Int, Int])
  checkAll("GroupDecoder[DateTime]", SerializableTests[GroupDecoder[DateTime]].serializable)

  checkAll("MatchDecoder[DateTime]", MatchDecoderTests[DateTime].decoder[Int, Int])
  checkAll("MatchDecoder[DateTime]", SerializableTests[MatchDecoder[DateTime]].serializable)

}
