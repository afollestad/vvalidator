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

import android.app.Activity
import android.view.View
import android.widget.Button
import com.afollestad.vvalidator.ValidationContainer
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
import com.afollestad.vvalidator.testutil.assertNotEmpty
import com.afollestad.vvalidator.testutil.assertNotNull
import com.afollestad.vvalidator.testutil.assertNull
import com.afollestad.vvalidator.testutil.assertSameAs
import com.afollestad.vvalidator.testutil.assertSize
import com.afollestad.vvalidator.testutil.assertTrue
import com.afollestad.vvalidator.testutil.assertType
import com.afollestad.vvalidator.testutil.second
import com.afollestad.vvalidator.testutil.triggerTextChanged
import com.afollestad.vvalidator.util.onTextChanged
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

    field.view.assertSameAs(activity.get().input)
    field.name.assertEqualTo("Input!")
  }

  @Test fun inputLayout() {
    form.inputLayout(ID_INPUT_LAYOUT, name = "Input layout!") {
      isNotEmpty()
    }
    val field = form.getFields()
        .single()
        .assertType<InputLayoutField>()

    field.view.assertSameAs(activity.get().inputLayout)
    field.name.assertEqualTo("Input layout!")
  }

  @Test fun spinner() {
    form.spinner(ID_SPINNER, name = "Spinner!") {
      selection().atLeast(0)
    }
    val field = form.getFields()
        .single()
        .assertType<SpinnerField>()

    field.view.assertSameAs(activity.get().spinner)
    field.name.assertEqualTo("Spinner!")
  }

  @Test fun checkable() {
    form.checkable(ID_CHECKABLE, name = "Checkable!") {
      isNotChecked()
    }
    val field = form.getFields()
        .single()
        .assertType<CheckableField>()

    field.view.assertSameAs(activity.get().checkable)
    field.name.assertEqualTo("Checkable!")
  }

  @Test fun seeker() {
    form.seeker(ID_SEEKER, name = "Seeker!") {
      progress().greaterThan(0)
    }
    val field = form.getFields()
        .single()
        .assertType<SeekField>()

    field.view.assertSameAs(activity.get().seeker)
    field.name.assertEqualTo("Seeker!")
  }

  @Test fun validate() {
    form.input(ID_INPUT, name = "Input") {
      isNotEmpty()
    }
    form.seeker(ID_SEEKER, name = "Seeker") {
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
              .second()
              .id.assertEqualTo(ID_SEEKER)

          this["Input"]!!.name.assertEqualTo("Input")
          this["Input"]!!.value.toString()
              .assertEmpty()
          this["Seeker"]!!.name.assertEqualTo("Seeker")
          this["Seeker"]!!.value.assertEqualTo(0)
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

  @Test fun destroy() {
    form.input(ID_INPUT, name = "Input!") {
      isNotEmpty()
    }

    form.container.assertNotNull()
    form.getFields()
        .assertNotEmpty()

    activity.destroy()

    form.container.assertNull()
    form.getFields()
        .assertEmpty()
  }

  @Test fun `start - do not use real time validation`() {
    form.realTimeValidationDebounce.assertEqualTo(-1)
    form.useRealTimeValidation.assertFalse()

    val dummyField = DummyInputField(activity.get())
    form.appendField(dummyField)
    form.start()

    dummyField.assertDidNotStartRealTimeValidation()
  }

  @Test fun `start - use real time validation`() {
    form.useRealTimeValidation(2500)

    form.realTimeValidationDebounce.assertEqualTo(2500)
    form.useRealTimeValidation.assertTrue()

    val dummyField = DummyInputField(activity.get())
    form.appendField(dummyField)
    form.start()

    dummyField.assertDidStartRealTimeValidation(2500)
  }

  @Test fun `start - use real time validation - disable submit with`() {
    val button = activity.get()
        .findViewById<Button>(ID_BUTTON)
    var didSubmit = false
    form.submitWith(ID_BUTTON) { didSubmit = true }
    button.isEnabled.assertTrue()

    form.useRealTimeValidation(
        debounce = 2500,
        disableSubmit = true
    )

    val dummyField = DummyInputField(activity.get())
    form.appendField(dummyField)
    form.start()

    button.isEnabled.assertFalse()
    button.performClick()
    didSubmit.assertFalse()

    dummyField.view.triggerTextChanged("")
    button.isEnabled.assertFalse()
    button.performClick()
    didSubmit.assertFalse()

    dummyField.view.triggerTextChanged("Hello")
    button.isEnabled.assertTrue()
    button.performClick()
    didSubmit.assertTrue()
  }
}

class DummyValidationContainer(
  private val activity: Activity
) : ValidationContainer(activity) {
  override fun <T : View> findViewById(id: Int): T? = activity.findViewById(id) as? T
}

class DummyInputField(
  activity: Activity
) : InputField(
    DummyValidationContainer(activity),
    activity.findViewById(ID_INPUT),
    "dummy input"
) {
  init {
    isNotEmpty()
  }

  private var didStartRealTimeValidation: Boolean = false
  private var realTimeDebounce: Int? = null

  override fun startRealTimeValidation(debounce: Int) {
    didStartRealTimeValidation = true
    realTimeDebounce = debounce
    view.onTextChanged { validate() }
  }

  fun assertDidStartRealTimeValidation(withDebounce: Int) {
    didStartRealTimeValidation.assertTrue()
    realTimeDebounce.assertEqualTo(withDebounce)
    didStartRealTimeValidation = false
    realTimeDebounce = null
  }

  fun assertDidNotStartRealTimeValidation() {
    didStartRealTimeValidation.assertFalse()
    realTimeDebounce.assertNull()
  }
}
