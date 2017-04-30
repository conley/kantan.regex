/*
 * Copyright 2017 Nicolas Rinaudo
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

import kantan.codecs.laws.discipline.SerializableTests
import kantan.regex.laws.discipline.{GroupDecoderTests, MatchDecoderTests}
import kantan.regex.laws.discipline.arbitrary._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline

class FloatDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  checkAll("GroupDecoder[Float]", GroupDecoderTests[Float].decoder[Int, Int])
  checkAll("GroupDecoder[Float]", SerializableTests[GroupDecoder[Float]].serializable)

  checkAll("MatchDecoder[Float]", MatchDecoderTests[Float].decoder[Int, Int])
  checkAll("MatchDecoder[Float]", SerializableTests[MatchDecoder[Float]].serializable)
}
