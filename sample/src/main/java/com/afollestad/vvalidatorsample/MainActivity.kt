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
package com.afollestad.vvalidatorsample

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.util.onItemSelected
import com.afollestad.vvalidator.util.showOrHide

/** @author Aidan Follestad (@afollestad) */
class MainActivity : AppCompatActivity() {
  private val checkBoxError by lazy { findViewById<TextView>(R.id.error_checkBox) }
  private val seekBarError by lazy { findViewById<TextView>(R.id.error_seekBar) }
  private val spinnerError by lazy { findViewById<TextView>(R.id.error_spinner) }
  private val inputSite by lazy { findViewById<EditText>(R.id.input_site) }
  private val spinner by lazy { findViewById<Spinner>(R.id.input_spinner) }
  private val labelSite by lazy { findViewById<TextView>(R.id.label_site) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    spinner.onItemSelected {
      labelSite.showOrHide(it > 0)
      inputSite.showOrHide(it > 0)
    }

    spinner.adapter = ArrayAdapter(
        this,
        R.layout.list_item_spinner,
        arrayOf("Select an option…", "I do not have a website", "I have a website!")
    ).apply {
      setDropDownViewResource(R.layout.list_item_spinner_dropdown)
    }

    form {
      useRealTimeValidation(disableSubmit = true)

      inputLayout(R.id.input_layout_name, name = "Name") {
        isNotEmpty().description("Enter your name!")
        length().atLeast(3)
      }

      input(R.id.input_email, name = "Email") {
        isNotEmpty()
        isEmail()
      }

      input(R.id.input_age, name = "Age") {
        isNotEmpty()
        isNumber()
      }

      input(R.id.input_weight, name = "Weight") {
        isNotEmpty()
        isDecimal().atMost(9999.99)
      }

      spinner(R.id.input_spinner, name = "Have a website") {
        selection()
            .atLeast(1)
            .description("Please make a selection")
        onErrors { _, errors ->
          val firstError = errors.firstOrNull()
          spinnerError.showOrHide(firstError != null)
          spinnerError.text = firstError?.toString()
        }
      }

      input(R.id.input_site, name = "Site", optional = true) {
        conditional({ spinner.selectedItemPosition > 1 }) {
          isUrl()
        }
      }

      input(R.id.input_bio, name = "Biography") {
        isNotEmpty()
        length().atLeast(20)
      }

      seeker(R.id.seek_bar, name = "Seek Bar") {
        progress()
            .atLeast(50)
            .description("selection must be at least 50%")
        onErrors { _, errors ->
          val firstError = errors.firstOrNull()
          seekBarError.showOrHide(firstError != null)
          seekBarError.text = firstError?.toString()
        }
      }

      checkable(R.id.check_terms, name = "Terms Agreement") {
        isChecked()
        onErrors { _, errors ->
          val firstError = errors.firstOrNull()
          checkBoxError.showOrHide(firstError != null)
          checkBoxError.text = firstError?.toString()
        }
      }

      submitWith(R.id.submit) { result ->
        toast("Success! Hello ${result["Name"]?.value}")
      }
    }
  }
}
