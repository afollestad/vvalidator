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
package com.afollestad.vvalidator.field.spinner

import android.widget.ArrayAdapter
import android.widget.Spinner
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.spinner.SpinnerAssertions.SelectionAssertion
import com.afollestad.vvalidator.field.FieldError
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.testutil.ID_SPINNER
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertSize
import com.afollestad.vvalidator.testutil.assertType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class SpinnerFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: SpinnerField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }

    activity.get()
        .findViewById<Spinner>(ID_SPINNER)
        .apply {
          adapter =
            ArrayAdapter<String>(activity.get(), android.R.layout.simple_list_item_1).apply {
              addAll("One", "Two", "Three", "Four", "Five", "Six")
            }
        }

    form = activity.get()
        .form {
          spinner(ID_SPINNER, name = "Spinner") {}
        }
    field = form.getFields()
        .single()
        .assertType()
    field.view.assertEqualTo(activity.get().spinner)
  }

  @Test fun selection() {
    val assertion = field.selection()
        .assertType<SelectionAssertion>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun assert_custom() {
    val assertion = field.assert("test") { true }
        .assertType<CustomViewAssertion<Spinner>>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun `real time validation off`() {
    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.selection()
        .atLeast(4)

    field.view.setSelection(5)
    errorsList.assertEmpty()

    field.view.setSelection(3)
    errorsList.assertEmpty()
  }

  @Test fun `real time validation on`() {
    field.startRealTimeValidation(0)

    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.selection()
        .atLeast(4)

    field.view.setSelection(5)
    errorsList.assertEmpty()

    field.view.setSelection(3)
    errorsList.assertSize(1)
    errorsList.single()
        .description.assertEqualTo("selection must be at least 4")
  }
}
