package shaiytan.irlbugtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bug.*
import models.BugType
import models.TheBug


class BugActivity : AppCompatActivity() {
    private fun setBugView(bug: TheBug) {
        header.text = bug.name
        rating.text = "Category: ${bug.category}    Rating:${bug.category.rating}"
        description.text = bug.description
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bug)
        val bug = Gson().fromJson(intent.getStringExtra("bug"), TheBug::class.java)
        setBugView(bug)
        val index = intent.getIntExtra("index", -1)
        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_view -> {
                    this display "View selected"
                    true
                }
                R.id.navigation_fix -> {
                    returnCommand(Command.FIX, index, bug.type)
                    true
                }
                R.id.navigation_edit -> {
                    returnCommand(Command.EDIT, index, bug.type)
                    true
                }
                R.id.navigation_delete -> {
                    returnCommand(Command.DELETE, index, bug.type)
                    true
                }
                else -> false
            }
        }
    }

    private fun returnCommand(command: Command, index: Int, type: BugType) {
        val intent = Intent()
        intent.putExtra("index", index)
        intent.putExtra("command", command)
        intent.putExtra("type", type)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
