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

package kantan.regex.enumeratum.values

import kantan.codecs.enumeratum.laws.discipline._
import kantan.codecs.laws.discipline.SerializableTests
import kantan.regex.{GroupDecoder, MatchDecoder}
import kantan.regex.enumeratum.arbitrary._
import kantan.regex.laws.discipline.{GroupDecoderTests, MatchDecoderTests}
import org.scalatest.FunSuite
import org.typelevel.discipline.scalatest.Discipline

class ByteEnumCodecTests extends FunSuite with Discipline {

  checkAll("GroupDecoder[EnumeratedByte]", SerializableTests[GroupDecoder[EnumeratedByte]].serializable)
  checkAll("MatchDecoder[EnumeratedByte]", SerializableTests[MatchDecoder[EnumeratedByte]].serializable)
  checkAll("GroupDecoder[EnumeratedByte]", GroupDecoderTests[EnumeratedByte].decoder[String, Float])
  checkAll("MatchDecoder[EnumeratedByte]", MatchDecoderTests[EnumeratedByte].decoder[String, Float])

}
