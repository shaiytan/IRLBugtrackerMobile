package shaiytan.irlbugtracker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import models.*

/**
 * Created by Shaiy'tan on 10.01.2018.
 */
class StubModel : Model {
    private val rawdata = """
        [
            {
                "id":1,
                "name":"осколки",
                "description":"возле ёлки лежат осколки разбитой игрушки",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"ANNOYING",
                "type":"BUG"
            },
            {
                "id":2,
                "name":"елка",
                "description":"надо снять мишуру и вынести ёлку в мусор",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IRRITATING",
                "type":"IMPROVEMENT"
            },
            {
                "id":3,
                "name":"блок питания",
                "description":"нужно отнеси в ремонт или купить новый блок питания",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"BUG"
            },
            {
                "id":4,
                "name":"пылесос",
                "description":"отремонтировать пылесос",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"BUG"
            },
            {
                "id":5,
                "name":"трансплантация",
                "description":"система для учета доноров органов (Spring?)",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"IDEA"
            },
            {
                "id":6,
                "name":"Магазин музла",
                "description":"музыкальный магазин (Mysql+Grails)",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":7,
                "name":"Инстаграмм",
                "description":"Убийца инстаграмма))0(Qt)",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":8,
                "name":"Munchkin",
                "description":"Оцифровка игры для андроида",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"IDEA"
            },
            {
                "id":9,
                "name":"Такси",
                "description":"Переписать систему такси на Spring+Javafx",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":10,
                "name":"Ластфм",
                "description":"клиент на телефон\n-список любимых композиций, отсортированный по прослушиваниям\n-список исполнителей, отсортированных по дате последнего прослушивания",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":11,
                "name":"Словарь",
                "description":"дописать словарь, переписать гуй на qml",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":12,
                "name":"Казино))",
                "description":"котлин/андроид ",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IDEA"
            },
            {
                "id":13,
                "name":"Вишлист",
                "description":"веб (джанго-реакт-котлин)?",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"IDEA"
            },
            {
                "id":14,
                "name":"дикий курсач",
                "description":"игра жизнь Qt",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IRRITATING",
                "type":"IDEA"
            },
            {
                "id":15,
                "name":"Учить",
                "description":"-java.util.concurrent\n-dagger\n-hibernate\n-spring",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IMPROVEMENT"
            },
            {
                "id":16,
                "name":"диплом",
                "description":"-переписать вьюху на джавафх\n-переписать сервер с DI\n-добавить андроид клиент\n-добавить веб клиент\n-доработать админ-панель",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"CRITICAL",
                "type":"IMPROVEMENT"
            },
            {
                "id":17,
                "name":"Бот",
                "description":"flash бот для musicwars",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"PERSPECTIVE",
                "type":"IDEA"
            },
            {
                "id":18,
                "name":"одежда",
                "description":"запустить стирку",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"CRITICAL",
                "type":"BUG"
            },
            {
                "id":19,
                "name":"толчок",
                "description":"помыть срочно",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"CRITICAL",
                "type":"BUG"
            },
            {
                "id":20,
                "name":"ведроид#1",
                "description":"написать активити со списками списки",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"CRITICAL",
                "type":"IMPROVEMENT"
            },
            {
                "id":21,
                "name":"ведроид#2",
                "description":"написать фрагмент с багвью",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IMPROVEMENT"
            },
            {
                "id":22,
                "name":"ведроид#3",
                "description":"написать активити с формой добавления или редактирования",
                "date":"Jan 9, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"IMPROVEMENT"
            },
            {
                "id":23,
                "name":"холодильник",
                "description":"помыть хотя бы верхнюю полку",
                "date":"Jan 10, 2018 12:00:00 AM",
                "category":"IMPORTANT",
                "type":"BUG"
            }
        ]
        """
    private val data = readgson(rawdata)

    private fun readgson(rawdata: String): List<TheBug> {
        val type = object : TypeToken<List<TheBug>>() {}.type!!
        return Gson().fromJson(rawdata, type)
    }

    override fun addBug(bug: TheBug) = 1 + (data.map(TheBug::id).max() ?: 0)

    override fun deleteBug(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBugById(id: Int) = data.find { it.id == id }

    override fun getBugs(type: BugType) = data.filter { it.type == type }

    override fun updateBug(bug: TheBug) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}