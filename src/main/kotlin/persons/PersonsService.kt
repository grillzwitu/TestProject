package persons

import Person
import Persons
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface PersonsService {
    suspend fun create(name: String, age: Int?): Int
    suspend fun all(): List<Person>
    suspend fun findById(id: Int): Person?
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

    override suspend fun findById(id: Int): Person? {
        val rec = transaction {
            addLogger(StdOutSqlLogger)
            Persons.select { Persons.id eq id }.firstOrNull()
        }

        return rec?.asPerson()
    }

}

private fun ResultRow.asPerson() = Person(this[Persons.id].value, this[Persons.name], this[Persons.age])
