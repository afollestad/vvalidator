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
package com.afollestad.vvalidator.assertion

import android.net.Uri
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutNotEmptyAssertion : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.editText?.text?.isNotEmpty() ?: false
  }

  override fun description(): String {
    return "cannot be empty"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutUrlAssertion : Assertion<TextInputLayout>() {
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
internal class InputLayoutUriAssertion(
  private val schemes: Array<out String>
) : Assertion<TextInputLayout>() {

  override fun isValid(view: TextInputLayout): Boolean {
    return try {
      val uri = Uri.parse(view.text())
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
internal class InputLayoutEmailAssertion : Assertion<TextInputLayout>() {
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
internal class InputLayoutNumberAssertion : Assertion<TextInputLayout>() {
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
internal class InputLayoutLengthExactlyAssertion(
  private val length: Int
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text().length == length
  }

  override fun description(): String {
    return "must be exactly $length characters"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutLengthLessThanAssertion(
  private val ceil: Int
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text().length < ceil
  }

  override fun description(): String {
    return "must be less than $ceil characters"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutLengthAtMostAssertion(
  private val ceil: Int
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text().length <= ceil
  }

  override fun description(): String {
    return "can only be $ceil characters at most"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutLengthAtLeastAssertion(
  private val floor: Int
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text().length >= floor
  }

  override fun description(): String {
    return "must be at least $floor characters"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutLengthGreaterThanAssertion(
  private val floor: Int
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text().length > floor
  }

  override fun description(): String {
    return "must be greater than $floor characters"
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutContainsAssertion(
  private val text: String
) : Assertion<TextInputLayout>() {
  override fun isValid(view: TextInputLayout): Boolean {
    return view.text()
        .contains(text)
  }

  override fun description(): String {
    return "must contain \"$text\""
  }
}

/** @author Aidan Follestad (@afollestad) */
internal class InputLayoutRegexAssertion(
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

private fun TextInputLayout.text(): String {
  return editText?.toString() ?: ""
}
