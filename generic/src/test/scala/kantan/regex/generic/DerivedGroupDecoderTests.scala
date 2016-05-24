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

package kantan.regex.generic

import kantan.codecs.shapeless.laws._
import kantan.codecs.shapeless.laws.discipline.arbitrary._
import kantan.regex.laws.discipline.GroupDecoderTests
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.typelevel.discipline.scalatest.Discipline
import scala.util.Try

class DerivedGroupDecoderTests extends FunSuite with GeneratorDrivenPropertyChecks with Discipline {
  implicit val arbLegal = arbLegalValue((o: Or[Int, Boolean]) ⇒ o match {
    case Left(i) ⇒ Option(i.toString)
    case Right(b) ⇒ Option(b.toString)
  })

  implicit val arbIllegal = arbIllegalValue[Option[String], Or[Int, Boolean]] { os ⇒
    os.forall(s ⇒ Try(s.toInt).isFailure && Try(s.toBoolean).isFailure)
  }

  checkAll("GroupDecoder[Or[Int, Boolean]]", GroupDecoderTests[Or[Int, Boolean]].decoder[Byte, String])
}
