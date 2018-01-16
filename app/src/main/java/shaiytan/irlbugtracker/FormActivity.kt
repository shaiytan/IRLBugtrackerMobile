package shaiytan.irlbugtracker

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.content_form.*
import models.*
import java.util.*

class FormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var currentBug: TheBug
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        edit_category.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Category.values())
        edit_type.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, BugType.values())
        val bug = Gson().fromJson(intent.getStringExtra("bug"), TheBug::class.java)
        setBugView(bug)
        fab.setOnClickListener { returnBug() }
        edit_date.setOnFocusChangeListener { _, focused ->
            if (focused) {
                showDateDialog()
            }
        }
    }

    private fun setBugView(bug: TheBug) {
        currentBug = bug
        edit_name.setText(currentBug.name)
        val c = Calendar.getInstance()
        c.time = currentBug.date
        val day = c[Calendar.DAY_OF_MONTH]
        val month = c[Calendar.MONTH]
        val year = c[Calendar.YEAR]
        edit_date.setText("$day/${month + 1}/$year")
        edit_category.setSelection(currentBug.category.ordinal)
        edit_type.setSelection(currentBug.type.ordinal)
        edit_description.setText(currentBug.description)
    }

    private fun returnBug() {
        val c = Calendar.getInstance()
        val (day, month, year) = edit_date.text.toString()
                .split("/")
                .map(Integer::valueOf)
                .take(3)
        c.set(year, month - 1, day)
        val date = c.time
        val bug = currentBug.copy(
                name = edit_name.text.toString(),
                description = edit_description.text.toString(),
                type = edit_type.selectedItem as BugType,
                category = edit_category.selectedItem as Category,
                date = date
        )
        val intent = Intent()
        intent.putExtra("bug", Gson().toJson(bug))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    private fun showDateDialog() {
        val (day, month, year) = edit_date.text.toString()
                .split("/")
                .map(Integer::valueOf)
                .take(3)
        val picker = DatePickerDialog(this, this, year, month - 1, day)
        picker.setTitle("Set Date")
        picker.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        edit_date.setText("$day/${month + 1}/$year")
    }

}
