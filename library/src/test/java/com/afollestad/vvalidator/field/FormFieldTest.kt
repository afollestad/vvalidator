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

import android.widget.EditText
import com.afollestad.vvalidator.assertion.InputAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.testutil.ID_INPUT
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertNull
import com.afollestad.vvalidator.testutil.assertTrue
import com.afollestad.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

private abstract class TestField : FormField<TestField, EditText>()

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class FormFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var field: TestField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    field = object : TestField() {
      override val id: Int = ID_INPUT

      override val name: String = "Test Input"

      override val view: EditText?
        get() = activity.get().input
    }
  }

  @Test fun assert() {
    val arg = NotEmptyAssertion()
    val result = field.assert(arg)
    result.assertEqualTo(arg)
    field.assertions()
        .single()
        .assertEqualTo(result)
  }

  @Test fun conditional() {
    val condition: Condition = { true }
    field.conditional(condition) {
      assert(NotEmptyAssertion())
    }

    val assertion = field.assertions()
        .single()
        .assertType<NotEmptyAssertion>()
    assertion.condition.assertEqualTo(condition)
  }

  @Test fun onErrors() {
    val onErrors: OnError<EditText> = { _, _ -> }
    field.onErrors(onErrors)
    field.onErrors.assertEqualTo(onErrors)
  }

  @Test fun validate() {
    var onErrorsCalled: FieldError? = null
    field.onErrors { view, errors ->
      view.assertEqualTo(field.view)
      onErrorsCalled = errors.singleOrNull()
    }

    val assertion = NotEmptyAssertion()
    field.assert(assertion)
    field.view!!.setText("")

    field.validate()
        .run {
          success().assertFalse()
          hasErrors().assertTrue()

          val error = errors().single()
          error.id.assertEqualTo(ID_INPUT)
          error.name.assertEqualTo("Test Input")
          error.description.assertEqualTo(assertion.description())

          onErrorsCalled.assertEqualTo(error)
        }

    field.view!!.setText("Hello")
    field.validate()
        .run {
          success().assertTrue()
          hasErrors().assertFalse()
          errors().assertEmpty()

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
    field.view!!.setText("")

    field.validate()
        .run {
          success().assertTrue()
          hasErrors().assertFalse()
          errors().assertEmpty()

          onErrorsCalled.assertNull()
        }
  }
}
