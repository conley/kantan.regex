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

import kantan.regex.implicits._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class CompilableOpsTests extends FunSuite with GeneratorDrivenPropertyChecks {
  test("asRegex should succeed for valid regular expressions") {
    assert("\\d+".asRegex[Int].isSuccess)
    assert("\\d+".asRegex[Int](1).isSuccess)

    assert(rx"\\d+".asRegex[Int].isSuccess)
    assert(rx"\\d+".asRegex[Int](1).isSuccess)

    assert("\\d+".r.asRegex[Int].isSuccess)
    assert("\\d+".r.asRegex[Int](1).isSuccess)
  }

  test("asRegex should fail for invalid regular expressions") {
    assert("[".asRegex[Int].isFailure)
    assert("[".asRegex[Int](1).isFailure)
  }

  test("asUnsafeRegex should succeed for valid regular expressions") {
    "\\d+".asUnsafeRegex[Int]
    "\\d+".asUnsafeRegex[Int](1)

    rx"\\d+".asUnsafeRegex[Int]
    rx"\\d+".asUnsafeRegex[Int](1)

    "\\d+".r.asUnsafeRegex[Int]
    "\\d+".r.asUnsafeRegex[Int](1)
    ()
  }

  test("asUnsafeRegex should fail for invalid regular expressions") {
    intercept[Exception]("[".asUnsafeRegex[Int])
    intercept[Exception]("[".asUnsafeRegex[Int](1))
    ()
  }
}
