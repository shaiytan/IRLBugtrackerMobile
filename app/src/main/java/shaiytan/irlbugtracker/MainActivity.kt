package shaiytan.irlbugtracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import android.support.v7.app.AppCompatActivity
import android.widget.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import models.*
import presenter.*
import views.View

enum class Command {
    NEW, FIX, EDIT, DELETE;
}

infix fun Activity.display(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

class MainActivity : AppCompatActivity(), View {
    companion object {
        val SHOW_BUG_REQUEST = 100
        val NEW_BUG_REQUEST = 101
        val EDIT_BUG_REQUEST = 102
    }

    private val datalists = mapOf<BugType, MutableList<String>>(
            BugType.BUG to mutableListOf(),
            BugType.IMPROVEMENT to mutableListOf(),
            BugType.IDEA to mutableListOf()
    )
    private var presenter: Presenter? = null
    private fun adapterOf(data: List<String>) = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
    private var lastSelected: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = BugPresenter(StubModel(), this)
        navigation.setOnNavigationItemSelectedListener { item ->
            val type = associatedType(item.itemId)
            bugslist.adapter = adapterOf(datalists[type]!!)
            true
        }
        bugslist.setOnItemClickListener { _, _, position, _ ->
            lastSelected = position
            presenter?.onBugSelected(position, associatedType(navigation.selectedItemId))
        }
        fab.setOnClickListener { _ ->
            presenter?.onAdd(associatedType(navigation.selectedItemId))
        }
    }

    private fun associatedId(type: BugType) = when (type) {
        BugType.BUG -> R.id.navigation_bugs
        BugType.IMPROVEMENT -> R.id.navigation_improvements
        BugType.IDEA -> R.id.navigation_ideas
    }

    private fun associatedType(id: Int) = when (id) {
        R.id.navigation_bugs -> BugType.BUG
        R.id.navigation_improvements -> BugType.IMPROVEMENT
        R.id.navigation_ideas -> BugType.IDEA
        else -> throw IllegalArgumentException()
    }

    private fun updateList(type: BugType, data: List<String>) {
        val list = datalists[type]!!
        list.clear()
        list.addAll(data)
        if (navigation.selectedItemId == associatedId(type))
            bugslist.adapter = adapterOf(data)
    }

    private fun showBugActivity(bug: TheBug) {
        val intent = Intent(this, BugActivity::class.java)
        intent.putExtra("bug", Gson().toJson(bug))
        intent.putExtra("index", lastSelected)
        startActivityForResult(intent, SHOW_BUG_REQUEST)
    }

    private fun onShowBugResult(data: Intent) {
        val index = data.getIntExtra("index", -1)
        val type = data.getSerializableExtra("type") as BugType
        when (data.getSerializableExtra("command") as Command) {
            Command.FIX -> presenter?.onFix(index, type)
            Command.EDIT -> presenter?.onEdit(index, type)
            Command.DELETE -> presenter?.onDelete(index, type)
            else -> throw IllegalArgumentException("Bug Activity returned invalid value")
        }
    }

    private fun showBugFormActivity(bug: TheBug, requestCode: Int) {
        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("bug", Gson().toJson(bug))
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (data != null) when (requestCode) {
            SHOW_BUG_REQUEST -> onShowBugResult(data)
            NEW_BUG_REQUEST ->
                presenter?.addBug(Gson().fromJson(data.getStringExtra("bug"), TheBug::class.java))
            EDIT_BUG_REQUEST ->
                presenter?.editBug(
                        Gson().fromJson(
                                data.getStringExtra("bug"),
                                TheBug::class.java))
            else -> throw IllegalArgumentException("Invalid request code on Activity result")
        }
    }

    override fun setBugData(bug: TheBug) {
        showBugActivity(bug)
    }

    override fun setBugsList(data: List<String>) {
        updateList(BugType.BUG, data)
    }

    override fun setIdeaData(bug: TheBug) {
        showBugActivity(bug)
    }

    override fun setIdeasList(data: List<String>) {
        updateList(BugType.IDEA, data)
    }

    override fun setImprovementData(bug: TheBug) {
        showBugActivity(bug)
    }

    override fun setImprovementsList(data: List<String>) {
        updateList(BugType.IMPROVEMENT, data)
    }

    override fun showBugForm(type: BugType) {
        showBugFormActivity(
                TheBug(id = 0, name = "", type = type),
                NEW_BUG_REQUEST
        )
    }

    override fun showBugForm(bug: TheBug) {
        showBugFormActivity(bug, EDIT_BUG_REQUEST)
    }
}
