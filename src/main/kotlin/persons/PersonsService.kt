package persons

import Person
import Persons
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface PersonsService {
    suspend fun create(name: String, age: Int?): Int
    suspend fun all(): List<Person>
}

class PersonsServiceDB: PersonsService {
    override suspend fun create(name: String, age: Int?): Int {
        val  id = transaction {
            Persons.insertAndGetId { person ->
                person[Persons.name] = name
                if (age != null) {
                    person[Persons.age] = age
                }
            }
        }
        return id.value
    }

    override suspend fun all(): List<Person> {
        return transaction {
            Persons.selectAll().map {row ->
                row.asPerson()
            }
        }
    }

}

private fun ResultRow.asPerson() = Person(this[Persons.id].value, this[Persons.name], this[Persons.age])
