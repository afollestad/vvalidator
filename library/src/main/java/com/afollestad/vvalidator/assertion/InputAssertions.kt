/**
 * Designed and developed by Aidan Follestad (@afollestad)
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
@file:Suppress("unused")

package com.afollestad.vvalidator.assertion

import android.net.Uri
import android.util.Patterns
import android.widget.EditText

sealed class InputAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class NotEmptyAssertion : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.isNotEmpty()
    }

    override fun description(): String {
      return "cannot be empty"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UrlAssertion : Assertion<EditText>() {
    private val regex = Patterns.WEB_URL

    override fun isValid(view: EditText): Boolean {
      return regex.matcher(view.text)
          .matches()
    }

    override fun description(): String {
      return "must be a valid URL"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion(
    private val schemes: Array<out String>
  ) : Assertion<EditText>() {

    override fun isValid(view: EditText): Boolean {
      return try {
        val uri = Uri.parse(view.text.toString())
        return if (schemes.isNotEmpty()) {
          uri.scheme in schemes
        } else {
          true
        }
      } catch (_: Exception) {
        false
      }
    }

    override fun description(): String {
      return if (schemes.isNotEmpty()) {
        "must be a valid URI with scheme in ${schemes.joinToString(prefix = "[", postfix = "]")}"
      } else {
        "must be a valid URI"
      }
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class EmailAssertion : Assertion<EditText>() {
    private val regex = Patterns.EMAIL_ADDRESS

    override fun isValid(view: EditText): Boolean {
      return regex.matcher(view.text)
          .matches()
    }

    override fun description(): String {
      return "must be a valid email address"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class NumberAssertion : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      if (view.text.isEmpty()) return false
      return view.text.toString().toIntOrNull() != null
    }

    override fun description(): String {
      return "must be a number"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthExactlyAssertion(
    private val length: Int
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.length == length
    }

    override fun description(): String {
      return "must be exactly $length characters"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthLessThanAssertion(
    private val ceil: Int
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.length < ceil
    }

    override fun description(): String {
      return "must be less than $ceil characters"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAtMostAssertion(
    private val ceil: Int
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.length <= ceil
    }

    override fun description(): String {
      return "can only be $ceil characters at most"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAtLeastAssertion(
    private val floor: Int
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.length >= floor
    }

    override fun description(): String {
      return "must be at least $floor characters"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthGreaterThanAssertion(
    private val floor: Int
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.length > floor
    }

    override fun description(): String {
      return "must be greater than $floor characters"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class ContainsAssertion(
    private val text: String
  ) : Assertion<EditText>() {
    override fun isValid(view: EditText): Boolean {
      return view.text.contains(text)
    }

    override fun description(): String {
      return "must contain \"$text\""
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class RegexAssertion(
    regexString: String,
    private val description: String
  ) : Assertion<EditText>() {
    private val regex = Regex(regexString)

    override fun isValid(view: EditText): Boolean {
      return view.text.matches(regex)
    }

    override fun description(): String {
      return description
    }
  }
}
