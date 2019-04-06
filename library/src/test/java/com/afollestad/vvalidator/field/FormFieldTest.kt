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
package com.afollestad.vvalidator.field

import android.view.View
import android.widget.EditText
import com.afollestad.vvalidator.ValidationContainer
import com.afollestad.vvalidator.assertion.input.InputAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.NumberAssertion
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Condition
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.testutil.ID_INPUT
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertNotNull
import com.afollestad.vvalidator.testutil.assertNull
import com.afollestad.vvalidator.testutil.assertSameAs
import com.afollestad.vvalidator.testutil.assertTrue
import com.afollestad.vvalidator.testutil.assertType
import com.afollestad.vvalidator.testutil.second
import com.afollestad.vvalidator.testutil.third
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

private class TestField(
  container: ValidationContainer,
  view: EditText,
  name: String
) : FormField<TestField, EditText, CharSequence>(container, view, name) {
  override fun obtainValue(
    id: Int,
    name: String
  ): FieldValue<CharSequence>? {
    return TextFieldValue(id, name, view.text)
  }

  override fun startRealTimeValidation(debounce: Int) = Unit
}

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class FormFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: TestField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    val container = object : ValidationContainer(activity.get()) {
      override fun <T : View> findViewById(id: Int): T? {
        @Suppress("UNCHECKED_CAST")
        return if (id == ID_INPUT) {
          return activity.get().input as T
        } else {
          null
        }
      }
    }
    field = TestField(container, activity.get().input, "Test Input")
    form = activity.get()
        .form { appendField(field) }
  }

  @Test fun assert() {
    val arg = NotEmptyAssertion()
    val result = field.assert(arg)
    result.assertEqualTo(arg)
    result.container.assertNotNull()
    field.assertions()
        .single()
        .assertEqualTo(result)
  }

  @Test fun conditional() {
    val condition1: Condition = { true }
    val condition2: Condition = { true }

    field.apply {
      assert(NotEmptyAssertion())
      conditional(condition1) {
        assert(ContainsAssertion("Hello, World!"))
        conditional(condition2) {
          assert(NumberAssertion())
        }
        assert(LengthAssertion())
      }
    }

    val firstAssertion = field.assertions()
        .first()
        .assertType<NotEmptyAssertion>()
    firstAssertion.conditions.assertEmpty()

    val secondAssertion = field.assertions()
        .second()
        .assertType<ContainsAssertion>()
    val secondConditions = secondAssertion.conditions.assertNotNull()
    secondConditions.single()
        .assertEqualTo(condition1)

    val thirdAssertion = field.assertions()
        .third()
        .assertType<NumberAssertion>()
    val thirdConditions = thirdAssertion.conditions.assertNotNull()
    thirdConditions.first()
        .assertSameAs(condition1)
    thirdConditions.second()
        .assertSameAs(condition2)

    val fourthAssertion = field.assertions()
        .last()
        .assertType<LengthAssertion>()
    val fourthConditions = fourthAssertion.conditions.assertNotNull()
    fourthConditions.single()
        .assertSameAs(condition1)
  }

  @Test fun onErrors() {
    val onErrors: OnError<EditText> = { _, _ -> }
    field.onErrors(onErrors)
    field.onErrors.assertEqualTo(onErrors)
  }

  @Test fun onValue() {
    val onValue: OnValue<EditText, CharSequence> = { _, _ -> }
    field.onValue(onValue)
    field.onValue.assertEqualTo(onValue)
  }

  @Test fun validate() {
    var onErrorsCalled: FieldError? = null
    field.onErrors { view, errors ->
      view.assertEqualTo(field.view)
      onErrorsCalled = errors.singleOrNull()
    }
    var emittedValue: FieldValue<CharSequence>? = null
    field.onValue { _, value ->
      emittedValue = value
    }

    val assertion = NotEmptyAssertion()
    field.assert(assertion)
    field.view.setText("")

    field.validate()
        .run {
          success().assertFalse()
          hasErrors().assertTrue()

          value!!.value.assertEmpty()
          emittedValue!!.value.assertEmpty()

          val error = errors().single()
          error.id.assertEqualTo(ID_INPUT)
          error.name.assertEqualTo("Test Input")
          error.description.assertEqualTo(assertion.defaultDescription())
          error.assertionType.assertEqualTo(NotEmptyAssertion::class)

          onErrorsCalled.assertEqualTo(error)
        }

    field.view.setText("Hello")
    field.validate()
        .run {
          success().assertTrue()
          hasErrors().assertFalse()
          errors().assertEmpty()

          value!!.value.toString()
              .assertEqualTo("Hello")
          emittedValue!!.value.toString()
              .assertEqualTo("Hello")

          onErrorsCalled.assertNull()
        }
  }

  @Test fun validate_hasConditions() {
    var onErrorsCalled: FieldError? = null
    field.onErrors { view, errors ->
      view.assertEqualTo(field.view)
      onErrorsCalled = errors.singleOrNull()
    }

    val assertion = NotEmptyAssertion()
    field.conditional(condition = { false }) {
      assert(assertion)
    }
    field.view.setText("")

    field.validate()
        .run {
          success().assertTrue()
          hasErrors().assertFalse()
          errors().assertEmpty()

          onErrorsCalled.assertNull()
        }
  }
}
