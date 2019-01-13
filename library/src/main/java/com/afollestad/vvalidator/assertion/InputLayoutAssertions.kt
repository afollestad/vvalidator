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
import com.google.android.material.textfield.TextInputLayout

/** @author Aidan Follestad (@afollestad) */
sealed class InputLayoutAssertions {

  /** @author Aidan Follestad (@afollestad) */
  class NotEmptyAssertion : Assertion<TextInputLayout>() {
    override fun isValid(view: TextInputLayout): Boolean {
      return view.editText?.text?.isNotEmpty() ?: false
    }

    override fun description(): String {
      return "cannot be empty"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UrlAssertion : Assertion<TextInputLayout>() {
    private val regex = Patterns.WEB_URL

    override fun isValid(view: TextInputLayout): Boolean {
      return regex.matcher(view.text())
          .matches()
    }

    override fun description(): String {
      return "must be a valid URL"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class UriAssertion : Assertion<TextInputLayout>() {

    private var schemes: Array<out String> = emptyArray()
    private var that: ((Uri) -> Boolean)? = null
    private var thatDescription: String? = null
    private var description: String? = null

    /** Asserts that the URI has a scheme within the given values. */
    fun hasScheme(vararg schemes: String) {
      this.schemes = schemes
    }

    /** Makes a custom assertion on the Uri. */
    fun that(
      description: String,
      that: (Uri) -> Boolean
    ) {
      this.thatDescription = description
      this.that = that
    }

    override fun isValid(view: TextInputLayout): Boolean {
      try {
        val uri = Uri.parse(view.text())
        if (schemes.isNotEmpty() && uri.scheme !in schemes) {
          description = "Scheme ${uri.scheme} not in ${schemes.displayString()}"
          return false
        }
        val thatResult = that?.invoke(uri) ?: true
        if (!thatResult) {
          description = thatDescription
          return false
        }
      } catch (_: Exception) {
        return false
      }
      return true
    }

    override fun description() = description ?: "must be a valid Uri"

    private fun Array<out String>.displayString(): String {
      return joinToString(prefix = "[", postfix = "]")
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class EmailAssertion : Assertion<TextInputLayout>() {
    private val regex = Patterns.EMAIL_ADDRESS

    override fun isValid(view: TextInputLayout): Boolean {
      return regex.matcher(view.text())
          .matches()
    }

    override fun description(): String {
      return "must be a valid email address"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class NumberAssertion : Assertion<TextInputLayout>() {
    override fun isValid(view: TextInputLayout): Boolean {
      val text = view.text()
      if (text.isEmpty()) return false
      return text.toIntOrNull() != null
    }

    override fun description(): String {
      return "must be a number"
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class LengthAssertion : Assertion<TextInputLayout>() {
    private var exactly: Int? = null
    private var lessThan: Int? = null
    private var atMost: Int? = null
    private var atLeast: Int? = null
    private var greaterThan: Int? = null

    /** Asserts the length is an exact (=) value. */
    fun exactly(length: Int) {
      exactly = length
    }

    /** Asserts the length is less than (<) a value. */
    fun lessThan(length: Int) {
      lessThan = length
    }

    /** Asserts the length is at most (<=) a value. */
    fun atMost(length: Int) {
      atMost = length
    }

    /** Asserts the length is at least (>=) a value. */
    fun atLeast(length: Int) {
      atLeast = length
    }

    /** Asserts the length is greater (>) than a value. */
    fun greaterThan(length: Int) {
      greaterThan = length
    }

    override fun isValid(view: TextInputLayout): Boolean {
      val length = view.text()
          .length
      return when {
        exactly != null -> length == exactly!!
        lessThan != null -> length < lessThan!!
        atMost != null -> length <= atMost!!
        atLeast != null -> length >= atLeast!!
        greaterThan != null -> length > greaterThan!!
        else -> false
      }
    }

    override fun description(): String {
      return when {
        exactly != null -> "must be exactly $exactly characters"
        lessThan != null -> "must be less than $lessThan characters"
        atMost != null -> "must be at most $atMost characters"
        atLeast != null -> "must be at least $atLeast characters"
        greaterThan != null -> "must be greater than $greaterThan characters"
        else -> "no length bound set"
      }
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class ContainsAssertion(
    private val text: String,
    private val ignoreCase: Boolean = false
  ) : Assertion<TextInputLayout>() {
    override fun isValid(view: TextInputLayout): Boolean {
      return view.text()
          .contains(text, ignoreCase)
    }

    override fun description(): String {
      return "must contain \"$text\""
    }
  }

  /** @author Aidan Follestad (@afollestad) */
  class RegexAssertion(
    regexString: String,
    private val description: String
  ) : Assertion<TextInputLayout>() {
    private val regex = Regex(regexString)

    override fun isValid(view: TextInputLayout): Boolean {
      return view.text()
          .matches(regex)
    }

    override fun description(): String {
      return description
    }
  }
}

internal fun TextInputLayout.text(): String {
  return editText?.toString() ?: ""
}
