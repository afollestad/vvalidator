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

package com.afollestad.vvalidator.field.input

import android.widget.EditText
import androidx.annotation.IdRes
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthAtLeastAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthAtMostAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthExactlyAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthGreaterThanAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthLessThanAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.UriAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.UrlAssertion
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.util.resName

/**
 * Represents an edit text field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class InputField internal constructor(
  container: ValidationContainer,
  @IdRes override val id: Int,
  override val name: String
) : FormField<InputField, EditText>() {

  init {
    onErrors { view, errors ->
      view.error = errors.firstOrNull()
          ?.toString()
    }
  }

  override val view = container.findViewById<EditText>(id) ?: throw IllegalArgumentException(
      "Didn't find view by ID ${id.resName(container.context())} in $container"
  )

  /** Asserts that the input text is not empty. */
  fun isNotEmpty() = assert(NotEmptyAssertion())

  /**
   * A wrapper around [conditional] which applies inner assertions only if the
   * input text is not empty.
   */
  fun isEmptyOr(builder: InputField.() -> Unit) {
    conditional(
        condition = {
          view.text.trim()
              .isNotEmpty()
        },
        builder = builder
    )
  }

  /** Asserts that the input text is a valid URL. */
  fun isUrl() = assert(UrlAssertion())

  /** Asserts that the input text is a valid URI, optionally with a specific scheme. */
  fun isUri(vararg schemes: String = emptyArray()) = assert(UriAssertion(schemes))

  /** Asserts that the input text is a valid email address. */
  fun isEmail() = assert(EmailAssertion())

  /** Asserts that the input text is a valid number. */
  fun isNumber() = assert(NumberAssertion())

  /** Asserts that the input text is shorter (<) than a specified length. */
  fun lengthLessThan(length: Int) = assert(LengthLessThanAssertion(length))

  /** Asserts that the input text is at most (<=) a specified length. */
  fun lengthAtMost(length: Int) = assert(LengthAtMostAssertion(length))

  /** Asserts that the input text is an exact (==) length. */
  fun lengthExactly(length: Int) = assert(LengthExactlyAssertion(length))

  /** Asserts that the input text is at least (>=) a specified length. */
  fun lengthAtLeast(length: Int) = assert(LengthAtLeastAssertion(length))

  /** Asserts that the input text is greater (>) than a specified length. */
  fun lengthGreaterThan(length: Int) = assert(LengthGreaterThanAssertion(length))

  /** Asserts that the input text contains a string. */
  fun contains(text: String) = assert(ContainsAssertion(text))

  /** Asserts that the input text matches a regular expression. */
  fun matches(
    description: String,
    regex: String
  ) = assert(
      RegexAssertion(
          regexString = regex,
          description = description
      )
  )

  /** Adds a custom inline assertion for the input field. */
  fun assert(
    description: String,
    matcher: (EditText) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))
}
