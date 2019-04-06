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
package com.afollestad.vvalidator.field.checkable

import android.widget.CompoundButton
import com.afollestad.vvalidator.assertion.CustomViewAssertion
import com.afollestad.vvalidator.assertion.checkable.CompoundButtonAssertions.CheckedStateAssertion
import com.afollestad.vvalidator.field.FieldError
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.testutil.ID_CHECKABLE
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.TestActivity
import com.afollestad.vvalidator.testutil.assertEmpty
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertNull
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
class CheckableFieldTest {

  private lateinit var activity: ActivityController<TestActivity>
  private lateinit var form: Form
  private lateinit var field: CheckableField

  @Before fun setup() {
    activity = Robolectric.buildActivity(TestActivity::class.java)
        .apply {
          create()
        }
    form = activity.get()
        .form {
          checkable(ID_CHECKABLE, name = "Checkable") {}
        }
    field = form.getFields()
        .single()
        .assertType()
    field.view.assertEqualTo(activity.get().checkable)
  }

  @Test fun isChecked() {
    val assertion = field.isChecked()
        .assertType<CheckedStateAssertion>()
    assertion.checked.assertTrue()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun isNotChecked() {
    val assertion = field.isNotChecked()
        .assertType<CheckedStateAssertion>()
    assertion.checked.assertFalse()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun assert_custom() {
    val assertion = field.assert("test") { true }
        .assertType<CustomViewAssertion<CompoundButton>>()
    field.assertions()
        .single()
        .assertEqualTo(assertion)
    assertion.conditions.assertEmpty()
  }

  @Test fun `real time validation off`() {
    field.isChecked()

    field.view.isChecked = true
    field.view.error.assertNull()

    field.view.isChecked = false
    field.view.error.assertNull()
  }

  @Test fun `real time validation on`() {
    val errorsList = mutableListOf<FieldError>()
    field.onErrors { _, errors -> errorsList.addAll(errors) }

    field.startRealTimeValidation(0)
    field.isChecked()

    field.view.isChecked = true
    errorsList.assertEmpty()

    field.view.isChecked = false
    errorsList.assertSize(1)
    errorsList.single()
        .description.assertEqualTo("should be checked")
  }
}
