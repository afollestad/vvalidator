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

import android.content.Context
import android.text.Editable
import android.util.Patterns
import android.view.ContextThemeWrapper
import androidx.test.core.app.ApplicationProvider
import com.afollestad.vvalidator.R
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.UriAssertion
import com.afollestad.vvalidator.assertion.InputLayoutAssertions.UrlAssertion
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.isEqualTo
import com.afollestad.vvalidator.testutil.isFalse
import com.afollestad.vvalidator.testutil.isTrue
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class InputLayoutAssertionsTest {

  private lateinit var view: TextInputLayout
  private lateinit var editText: TextInputEditText

  @Before fun setup() {
    val appContext = ApplicationProvider.getApplicationContext<Context>()
    val context =
      ContextThemeWrapper(appContext, R.style.Theme_MaterialComponents_Light)
    editText = TextInputEditText(context)
    view = TextInputLayout(context).apply {
      isHintAnimationEnabled = false
    }
    view.addView(editText)
  }

  @Test fun notEmpty() {
    val assertion = NotEmptyAssertion()

    editText.text = "test".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("cannot be empty")
  }

  @Test fun isUrl() {
    val assertion = UrlAssertion()

    editText.text = "https://af.codes/test.html".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "Hello, World!".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be a valid URL")
  }

  @Test fun isUri_withSchemes() {
    val assertion = UriAssertion().hasScheme(
        "expected a file or ftp Uri",
        listOf("file", "ftp")
    )

    editText.text = "file://storage/external".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "content://storage/external".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("expected a file or ftp Uri")
  }

  @Test fun isUri_withThat() {
    val assertion = UriAssertion()
        .that("have q param") {
          !it.getQueryParameter("q").isNullOrEmpty()
        }

    editText.text = "https://af.codes?q=test".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "https://af.codes".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("have q param")
  }

  @Test fun isEmail() {
    val assertion = EmailAssertion()

    editText.text = "tchalla@wakana.gov".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "testing".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be a valid email address")
  }

  @Test fun isNumber() {
    val assertion = NumberAssertion()

    editText.text = "1".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "a".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be a number")
  }

  @Test fun isNumber_exactly() {
    val assertion = NumberAssertion().apply {
      exactly(5)
    }

    editText.text = "5".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "1".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must equal 5")
  }

  @Test fun isNumber_lessThan() {
    val assertion = NumberAssertion().apply {
      lessThan(5)
    }

    editText.text = "4".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "5".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be less than 5")
  }

  @Test fun isNumber_atMost() {
    val assertion = NumberAssertion().apply {
      atMost(5)
    }

    editText.text = "4".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "5".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "6".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be at most 5")
  }

  @Test fun isNumber_atLeast() {
    val assertion = NumberAssertion().apply {
      atLeast(5)
    }

    editText.text = "5".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "6".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "4".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be at least 5")
  }

  @Test fun isNumber_greaterThan() {
    val assertion = NumberAssertion().apply {
      greaterThan(5)
    }

    editText.text = "6".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "7".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "5".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be greater than 5")
  }

  @Test fun length() {
    val assertion = LengthAssertion()

    editText.text = "1".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("no length bound set")
  }

  @Test fun length_exactly() {
    val assertion = LengthAssertion().apply {
      exactly(5)
    }

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hell".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be exactly 5")

    editText.text = "helloo".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be exactly 5")
  }

  @Test fun length_lessThan() {
    val assertion = LengthAssertion().apply {
      lessThan(5)
    }

    editText.text = "hell".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be less than 5")

    editText.text = "hello,".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be less than 5")
  }

  @Test fun length_atMost() {
    val assertion = LengthAssertion().apply {
      atMost(5)
    }

    editText.text = "hell".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello,".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be at most 5")
  }

  @Test fun length_atLeast() {
    val assertion = LengthAssertion().apply {
      atLeast(5)
    }

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello,".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hell".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be at least 5")
  }

  @Test fun length_greaterThan() {
    val assertion = LengthAssertion().apply {
      greaterThan(5)
    }

    editText.text = "hello,".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("length must be greater than 5")
  }

  @Test fun contains() {
    val assertion = ContainsAssertion("World")

    editText.text = "Hello World".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "Hello world".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must contain \"World\"")
  }

  @Test fun contains_ignoreCase() {
    val assertion = ContainsAssertion("World").apply {
      ignoreCase()
    }

    editText.text = "hello world".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must contain \"World\"")
  }

  @Test fun regex() {
    val regex = Patterns.IP_ADDRESS.pattern()
    val assertion = RegexAssertion(regex, "must be an IP address")

    editText.text = "192.168.0.1".toEditable()
    assertion.isValid(view)
        .isTrue()

    editText.text = "hello".toEditable()
    assertion.isValid(view)
        .isFalse()
    assertion.description()
        .isEqualTo("must be an IP address")
  }

  private fun String.toEditable(): Editable {
    return Editable.Factory.getInstance()
        .newEditable(this)
  }
}
