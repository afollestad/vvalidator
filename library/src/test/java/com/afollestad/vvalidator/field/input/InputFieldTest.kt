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
package com.afollestad.vvalidator.field.input

import android.widget.EditText
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.UriAssertion
import com.afollestad.vvalidator.assertion.InputAssertions.UrlAssertion
import com.afollestad.vvalidator.field.FieldError
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.testutil.ID_INPUT
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertNotNull
import com.afollestad.vvalidator.testutil.assertNull
import com.afollestad.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class InputFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: InputField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    form = activity.get()
        .form {
          input(ID_INPUT, name = "Input") {}
        }
    field = form.getFields()
        .single()
        .assertType()
    field.view.assertEqualTo(activity.get().input)
  }

  @Test fun isNotEmpty() {
    val assertion = field.isNotEmpty()
        .assertType<NotEmptyAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun isUrl() {
    val assertion = field.isUrl()
        .assertType<UrlAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun isUri() {
    val assertion = field.isUri()
        .assertType<UriAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun isEmail() {
    val assertion = field.isEmail()
        .assertType<EmailAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun isNumber() {
    val assertion = field.isNumber()
        .assertType<NumberAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun length() {
    val assertion = field.length()
        .assertType<LengthAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun contains() {
    val assertion = field.contains("hello")
        .assertType<ContainsAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun matches() {
    val assertion = field.matches("test regex", "hello|world")
        .assertType<RegexAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun assert_custom() {
    val assertion = field.assert("test") { true }
        .assertType<CustomViewAssertion<EditText>>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.condition.assertNull()
  }

  @Test fun isEmptyOr() {
    field.isEmptyOr { isUrl() }
    val assertion = field.assertions()
        .single()
        .assertType<UrlAssertion>()
    assertion.condition.assertNotNull()
  }

  @Test fun onErrors() {
    val errors = listOf(
        FieldError(
            id = ID_INPUT,
            name = "Input",
            description = "must not be empty",
            assertionType = NotEmptyAssertion::class
        )
    )
    field.propagateErrors(errors)
    field.view.error.assertEqualTo("Input must not be empty")
  }
}
