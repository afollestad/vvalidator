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

import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.NumberDecimalAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.input.InputLayoutAssertions.UriAssertion
import com.afollestad.vvalidator.assertion.input.text
import com.afollestad.vvalidator.field.FieldValue
import com.afollestad.vvalidator.field.FormField
import com.afollestad.vvalidator.field.TextFieldValue
import com.afollestad.vvalidator.util.onTextChanged
import com.google.android.material.textfield.TextInputLayout

/**
 * Represents an edit text field.
 *
 * @author Aidan Follestad (@afollestad)
 */
class InputLayoutField(
  container: ValidationContainer,
  view: TextInputLayout,
  name: String?
) : FormField<InputLayoutField, TextInputLayout, CharSequence>(container, view, name) {
  init {
    onErrors { _, errors ->
      view.error = errors.firstOrNull()
          ?.toString()
    }
  }

  /** The TextInputEditText that's inside of the TextInputLayout. */
  val editText = view.editText
      ?: error("TextInputLayout ${container.getFieldName(view.id)} should have a child EditText.")

  /** Asserts that the input text is not empty. */
  fun isNotEmpty() = assert(NotEmptyAssertion())

  /**
   * A wrapper around [conditional] which applies inner assertions only if the
   * input text is not empty.
   */
  fun isEmptyOr(builder: InputLayoutField.() -> Unit) = conditional(
      condition = {
        view.text()
            .trim()
            .isNotEmpty()
      },
      builder = builder
  )

  /** Asserts that the input text is a valid web address (HTTP or HTTPS). */
  fun isUrl() = assert(UriAssertion())
      .hasScheme("http", "https")
      .that { !it.host.isNullOrEmpty() }

  /** Asserts that the input text is a valid URI. */
  fun isUri() = assert(UriAssertion())

  /** Asserts that the input text is a valid email address. */
  fun isEmail() = assert(EmailAssertion())

  /** Asserts that the input text is a valid number. */
  fun isNumber() = assert(NumberAssertion())

  /** Asserts that the input text is a valid decimal. */
  fun isDecimal() = assert(NumberDecimalAssertion())

  /** Asserts that the input text contains a string. */
  fun length() = assert(LengthAssertion())

  /** Asserts that the input text contains a string. */
  fun contains(text: String) = assert(ContainsAssertion(text))

  /** Asserts that the input text matches a regular expression. */
  fun matches(regex: String) = assert(RegexAssertion(regex))

  /** Adds a custom inline assertion for the input layout field. */
  fun assert(
    description: String,
    matcher: (TextInputLayout) -> Boolean
  ) = assert(CustomViewAssertion(description, matcher))

  /** Return a snapshot of the [TextInputLayout.editText]'s text. **/
  override fun obtainValue(
    id: Int,
    name: String
  ): FieldValue<CharSequence>? {
    val currentValue = editText.text as? CharSequence ?: return null
    return TextFieldValue(
        id = id,
        name = name,
        value = currentValue
    )
  }

  override fun startRealTimeValidation(debounce: Int) {
    view.editText?.onTextChanged(debounce) { validate() }
  }
}
