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
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.util.onItemSelected
import com.afollestad.vvalidator.util.showOrHide
import kotlinx.android.synthetic.main.activity_main.error_checkBox as checkBoxError
import kotlinx.android.synthetic.main.activity_main.error_seekBar as seekBarError
import kotlinx.android.synthetic.main.activity_main.error_spinner as spinnerError
import kotlinx.android.synthetic.main.activity_main.input_site as inputSite
import kotlinx.android.synthetic.main.activity_main.input_spinner as spinner
import kotlinx.android.synthetic.main.activity_main.label_site as labelSite

/** @author Aidan Follestad (@afollestad) */
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    spinner.onItemSelected {
      labelSite.showOrHide(it > 0)
      inputSite.showOrHide(it > 0)
    }

    spinner.adapter = ArrayAdapter<String>(
        this,
        R.layout.list_item_spinner,
        arrayOf("Select an option…", "I do not have a website", "I have a website!")
    ).apply {
      setDropDownViewResource(R.layout.list_item_spinner_dropdown)
    }

    form {
      inputLayout(R.id.input_layout_name, name = "Name") {
        isNotEmpty()
        lengthAtLeast(3)
      }

      input(R.id.input_email, name = "Email") {
        isNotEmpty()
        isEmail()
      }

      input(R.id.input_age, name = "Age") {
        isNotEmpty()
        isNumber()
      }

      spinner(R.id.input_spinner, name = "Have a website") {
        selectedPositionAtLeast(1)
        onErrors { _, errors ->
          val firstError = errors.firstOrNull()
          spinnerError.showOrHide(firstError != null)
          spinnerError.text = firstError?.toString()
        }
      }

      input(R.id.input_site, name = "Site") {
        conditional({ spinner.selectedItemPosition > 1 }) {
          isNotEmpty()
          isUrl()
        }
      }

      input(R.id.input_bio, name = "Biography") {
        isNotEmpty()
        lengthAtLeast(20)
      }

      seeker(R.id.seek_bar, name = "Seek Bar") {
        progressAtLeast(50)
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

      submitWith(R.id.submit) {
        toast("Success!")
      }
    }
  }
}
