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
package com.afollestad.vvalidator.assertion.input

import android.content.Context
import android.text.Editable
import android.util.Patterns
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import com.afollestad.vvalidator.assertion.input.InputAssertions.ContainsAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.EmailAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.LengthAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.NotEmptyAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.NumberAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.NumberDecimalAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.RegexAssertion
import com.afollestad.vvalidator.assertion.input.InputAssertions.UriAssertion
import com.afollestad.vvalidator.testutil.NoManifestTestRunner
import com.afollestad.vvalidator.testutil.assertEqualTo
import com.afollestad.vvalidator.testutil.assertFalse
import com.afollestad.vvalidator.testutil.assertNotEqualTo
import com.afollestad.vvalidator.testutil.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/** @author Aidan Follestad (@afollestad) */
@RunWith(NoManifestTestRunner::class)
class InputAssertionsTest {

  private lateinit var view: EditText

  @Before fun setup() {
    val appContext = ApplicationProvider.getApplicationContext<Context>()
    view = EditText(appContext)
  }

  @Test fun notEmpty() {
    val assertion = NotEmptyAssertion()

    view.text = "test".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("cannot be empty")
  }

  @Test fun isUri_withSchemes() {
    val assertion = UriAssertion().hasScheme("file", "ftp")

    view.text = "file://storage/external".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "content://storage/external".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("scheme 'content' not in ['file', 'ftp']")
  }

  @Test fun isUri_withThat() {
    val assertion = UriAssertion()
        .that {
          !it.getQueryParameter("q").isNullOrEmpty()
        }

    view.text = "https://af.codes?q=test".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "https://af.codes".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("didn't pass custom validation")
  }

  @Test fun isEmail() {
    val assertion = EmailAssertion()

    view.text = "tchalla@wakana.gov".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "testing".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("must be a valid email address")
  }

  @Test fun isNumber() {
    val assertion = NumberAssertion()

    view.text = "1".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "a".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be a number")
  }

  @Test fun isNumber_exactly() {
    val assertion = NumberAssertion().apply {
      exactly(5)
    }

    view.text = "5".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "1".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be exactly 5")
  }

  @Test fun isNumber_lessThan() {
    val assertion = NumberAssertion().apply {
      lessThan(5)
    }

    view.text = "4".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be less than 5")
  }

  @Test fun isNumber_atMost() {
    val assertion = NumberAssertion().apply {
      atMost(5)
    }

    view.text = "4".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "6".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be at most 5")
  }

  @Test fun isNumber_atLeast() {
    val assertion = NumberAssertion().apply {
      atLeast(5)
    }

    view.text = "5".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "6".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "4".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be at least 5")
  }

  @Test fun isNumber_greaterThan() {
    val assertion = NumberAssertion().apply {
      greaterThan(5)
    }

    view.text = "6".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "7".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be greater than 5")
  }

  @Test fun isDecimal() {
    val assertion = NumberDecimalAssertion()

    view.text = "1.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "a".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be a number")
  }

  @Test fun isDecimal_exactly() {
    val assertion = NumberDecimalAssertion().apply {
        exactly(5.0)
    }

    view.text = "5.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "1.0".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be exactly 5.0")
  }

  @Test fun isDecimal_lessThan() {
    val assertion = NumberDecimalAssertion().apply {
        lessThan(5.0)
    }

    view.text = "4.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5.0".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be less than 5.0")
  }

  @Test fun isDecimal_atMost() {
    val assertion = NumberDecimalAssertion().apply {
        atMost(5.0)
    }

    view.text = "4.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "6.0".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be at most 5.0")
  }

  @Test fun isDecimal_atLeast() {
    val assertion = NumberDecimalAssertion().apply {
        atLeast(5.0)
    }

    view.text = "5.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "6.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "4.0".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be at least 5.0")
}

  @Test fun isDecimal_greaterThan() {
    val assertion = NumberDecimalAssertion().apply {
      greaterThan(5.0)
    }

    view.text = "6.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "7.0".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "5.0".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("value must be greater than 5.0")
  }

  @Test fun length() {
    val assertion = LengthAssertion()

    view.text = "1".toEditable()
    assertion.isValid(view)
        .assertTrue()
    assertion.defaultDescription()
        .assertEqualTo("no length bound set")
  }

  @Test fun length_exactly() {
    val assertion = LengthAssertion().apply {
      exactly(5)
    }

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hell".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be exactly 5")

    view.text = "helloo".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("length must be exactly 5")
  }

  @Test fun length_lessThan() {
    val assertion = LengthAssertion().apply {
      lessThan(5)
    }

    view.text = "hell".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be less than 5")

    view.text = "hello,".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be less than 5")
  }

  @Test fun length_atMost() {
    val assertion = LengthAssertion().apply {
      atMost(5)
    }

    view.text = "hell".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello,".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be at most 5")
  }

  @Test fun length_atLeast() {
    val assertion = LengthAssertion().apply {
      atLeast(5)
    }

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello,".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hell".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be at least 5")
  }

  @Test fun length_greaterThan() {
    val assertion = LengthAssertion().apply {
      greaterThan(5)
    }

    view.text = "hello,".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be greater than 5")
  }

  @Test fun length_chained_range() {
    val assertion = LengthAssertion().apply {
      greaterThan(5)
      lessThan(7)
    }

    view.text = "hello,".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello,.".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be greater than 5, less than 7")
  }

  @Test fun length_chained_range_atMost() {
    val assertion = LengthAssertion().apply {
      atMost(5)
      lessThan(7)
    }

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertTrue(assertion.defaultDescription())

    view.text = "hello,.".toEditable()
    assertion.isValid(view)
        .assertFalse(assertion.defaultDescription())
    assertion.defaultDescription()
        .assertEqualTo("length must be at most 5, less than 7")
  }

  @Test fun contains() {
    val assertion = ContainsAssertion("World")

    view.text = "Hello World".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "Hello world".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("must contain \"World\"")
  }

  @Test fun contains_ignoreCase() {
    val assertion = ContainsAssertion("World").apply {
      ignoreCase()
    }

    view.text = "hello world".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.defaultDescription()
        .assertEqualTo("must contain \"World\"")
  }

  @Test fun regex() {
    val regex = Patterns.IP_ADDRESS.pattern()
    val assertion = RegexAssertion(regex)
        .description("must be an IP address")

    view.text = "192.168.0.1".toEditable()
    assertion.isValid(view)
        .assertTrue()

    view.text = "hello".toEditable()
    assertion.isValid(view)
        .assertFalse()
    assertion.description()
        .assertEqualTo("must be an IP address")
    assertion.defaultDescription()
        .assertNotEqualTo("must be an IP address")
  }

  private fun String.toEditable(): Editable {
    return Editable.Factory.getInstance()
        .newEditable(this)
  }
}
