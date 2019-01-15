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
package com.afollestad.vvalidator.form

import com.afollestad.vvalidator.field.checkable.CheckableField
import com.afollestad.vvalidator.field.input.InputField
import com.afollestad.vvalidator.field.input.InputLayoutField
import com.afollestad.vvalidator.field.seeker.SeekField
import com.afollestad.vvalidator.field.spinner.SpinnerField
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.testutil.ID_BUTTON
import com.afollestad.vvalidator.testutil.ID_CHECKABLE
import com.afollestad.vvalidator.testutil.ID_INPUT
import com.afollestad.vvalidator.testutil.ID_INPUT_LAYOUT
import com.afollestad.vvalidator.testutil.ID_SEEKER
import com.afollestad.vvalidator.testutil.ID_SPINNER
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertSize
import com.afollestad.vvalidator.testutil.assertTrue
import com.afollestad.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class FormTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    form = activity.get()
        .form { }
  }

  @Test fun input() {
    form.input(ID_INPUT, name = "Input!") {
      isNotEmpty()
    }
    val field = form.getFields()
        .single()
        .assertType<InputField>()

    field.view.assertEqualTo(activity.get().input)
    field.id.assertEqualTo(ID_INPUT)
    field.name.assertEqualTo("Input!")
  }

  @Test fun inputLayout() {
    form.inputLayout(ID_INPUT_LAYOUT, name = "Input layout!") {
      isNotEmpty()
    }
    val field = form.getFields()
        .single()
        .assertType<InputLayoutField>()

    field.view.assertEqualTo(activity.get().inputLayout)
    field.id.assertEqualTo(ID_INPUT_LAYOUT)
    field.name.assertEqualTo("Input layout!")
  }

  @Test fun spinner() {
    form.spinner(ID_SPINNER, name = "Spinner!") {
      selection().atLeast(0)
    }
    val field = form.getFields()
        .single()
        .assertType<SpinnerField>()

    field.view.assertEqualTo(activity.get().spinner)
    field.id.assertEqualTo(ID_SPINNER)
    field.name.assertEqualTo("Spinner!")
  }

  @Test fun checkable() {
    form.checkable(ID_CHECKABLE, name = "Checkable!") {
      isNotChecked()
    }
    val field = form.getFields()
        .single()
        .assertType<CheckableField>()

    field.view.assertEqualTo(activity.get().checkable)
    field.id.assertEqualTo(ID_CHECKABLE)
    field.name.assertEqualTo("Checkable!")
  }

  @Test fun seeker() {
    form.seeker(ID_SEEKER, name = "Seeker!") {
      progress().greaterThan(0)
    }
    val field = form.getFields()
        .single()
        .assertType<SeekField>()

    field.view.assertEqualTo(activity.get().seeker)
    field.id.assertEqualTo(ID_SEEKER)
    field.name.assertEqualTo("Seeker!")
  }

  @Test fun validate() {
    form.input(ID_INPUT, name = "Input!") {
      isNotEmpty()
    }
    form.seeker(ID_SEEKER, name = "Seeker!") {
      progress().greaterThan(1)
    }
    form.getFields()
        .assertSize(2)

    form.validate()
        .run {
          success().assertFalse()
          hasErrors().assertTrue()
          errors()
              .first()
              .id.assertEqualTo(ID_INPUT)
          errors()
              .last()
              .id.assertEqualTo(ID_SEEKER)
        }

    activity.get()
        .run {
          input.setText("Hello, World!")
          seeker.progress = 2
        }
    form.validate()
        .run {
          success().assertTrue()
          hasErrors().assertFalse()
          errors().assertEmpty()
        }
  }

  @Test fun submitWith_button_success() {
    var called = false
    form.submitWith(ID_BUTTON) { result ->
      result.success()
          .assertTrue()
      called = true
    }
    form.getFields()
        .assertEmpty()

    activity.get()
        .button.performClick()
    called.assertTrue()
  }

  @Test fun submitWith_button_failure() {
    form.input(ID_INPUT, name = "Input!") {
      isNotEmpty()
    }
    form.seeker(ID_SEEKER, name = "Seeker!") {
      progress().greaterThan(1)
    }
    form.getFields()
        .assertSize(2)

    var called = false
    form.submitWith(ID_BUTTON) { result ->
      result.success()
          .assertTrue()
      called = true
    }

    activity.get()
        .button.performClick()
    called.assertFalse()
  }
}
