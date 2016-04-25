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

import java.util.regex.Matcher
import kantan.codecs.Result

class Match private[regex] (private val matcher: Matcher) {
  def length: Int = matcher.groupCount()

  def decode[A](index: Int)(implicit da: GroupDecoder[A]): DecodeResult[A] =
    if(index < 0 || index > length) DecodeResult.noSuchGroupId(index)
    else                            da.decode(matcher.group(index))

  def decode[A](name: String)(implicit da: GroupDecoder[A]): DecodeResult[A] =
    Result.nonFatalOr(DecodeError.NoSuchGroupName(name))(matcher.group(name)).flatMap { v ⇒
      if(v == null) DecodeResult.noSuchGroupName(name)
      else          da.decode(v)
    }
}
