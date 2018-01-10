package shaiytan.irlbugtracker

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import models.*
import presenter.*
import views.View

class MainActivity : AppCompatActivity(), View {
    private val datalists = mapOf<BugType, MutableList<String>>(
            BugType.BUG to mutableListOf(),
            BugType.IMPROVEMENT to mutableListOf(),
            BugType.IDEA to mutableListOf()
    )
    private var presenter: Presenter? = null
    private fun adapterOf(data: List<String>) = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val type = when (item.itemId) {
            R.id.navigation_bugs -> BugType.BUG
            R.id.navigation_improvements -> BugType.IMPROVEMENT
            R.id.navigation_ideas -> BugType.IDEA
            else -> return@OnNavigationItemSelectedListener false
        }
        bugslist.adapter = adapterOf(datalists[type]!!)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = BugPresenter(StubModel(), this)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private infix fun display(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setBugData(bug: TheBug) {
        this display "setBugData()"
    }

    private fun associatedId(type: BugType) = when (type) {
        BugType.BUG -> R.id.navigation_bugs
        BugType.IMPROVEMENT -> R.id.navigation_improvements
        BugType.IDEA -> R.id.navigation_ideas
    }

    private fun updateList(type: BugType, data: List<String>) {
        val list = datalists[type]!!
        list.clear()
        list.addAll(data)
        if (navigation.selectedItemId == associatedId(type))
            bugslist.adapter = adapterOf(data)
    }

    override fun setBugsList(data: List<String>) {
        updateList(BugType.BUG, data)
    }

    override fun setIdeaData(bug: TheBug) {
        this display "setIdeaData()"
    }

    override fun setIdeasList(data: List<String>) {
        updateList(BugType.IDEA, data)
    }

    override fun setImprovementData(bug: TheBug) {
        this display "setImprovementData()"
    }

    override fun setImprovementsList(data: List<String>) {
        updateList(BugType.IMPROVEMENT, data)
    }

    override fun showBugForm(type: BugType) {
        this display "display new bug form"
    }

    override fun showBugForm(bug: TheBug) {
        this display "display edit bug form"
    }
}
