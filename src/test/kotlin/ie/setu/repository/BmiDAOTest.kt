package ie.setu.repository


import ie.setu.domain.Bmi
import ie.setu.domain.db.Bmis
import ie.setu.domain.db.Users
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.bmis
import ie.setu.helpers.populateBmiTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

//retrieving some test data from Fixtures
val bmi1 = bmis[0]
val bmi2 = bmis[1]
val bmi3 = bmis[2]

class BmiDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadBmis {
        @Test
        fun `getting all bmi from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three bmi
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act and Assert
                assertEquals(3, bmiDAO.getAll().size)
            }
        }

        @Test
        fun `get bmi by id that doesn't exist, results in no bmi returned`() {
            transaction {

                //Arrange - create and populate table with three bmis
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act & Assert
                assertEquals(null, bmiDAO.findByBmiId(4))
            }
        }

        @Test
        fun `get bmi by id that exists, results in a correct bmi returned`() {
            transaction {

                //Arrange - create and populate table with three bmis
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act & Assert
                assertEquals(bmi1, bmiDAO.findByBmiId(1))
                assertEquals(bmi3, bmiDAO.findByBmiId(3))
            }
        }

        @Test
        fun `get bmis by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three bmis
                val userDAO = populateUserTable()
                val bmiDAO = populateBmiTable()
                assertEquals(2, bmiDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get bmi by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three bmi
                val userDAO = populateUserTable()
                val bmiDAO = populateBmiTable()
                //Act & Assert
                assertEquals(0, bmiDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all bmi over empty table return null`() {
            transaction {
                //Arrange - create and set up bmiDAO object
                SchemaUtils.create(Bmis)
                val bmiDAO = BmiDAO()

                //Act and assert
                assertEquals(0, bmiDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateBmi {
        @Test
        fun `multiple bmi added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three bmi
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act & Assert
                assertEquals(3, bmiDAO.getAll().size)
                assertEquals(bmi1, bmiDAO.findByBmiId(bmi1.id))
                assertEquals(bmi2, bmiDAO.findByBmiId(bmi2.id))
                assertEquals(bmi3, bmiDAO.findByBmiId(bmi3.id))
            }
        }
    }

    @Nested
    inner class DeleteBmis {
        @Test
        fun `deleting a non-existant bmi in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 bmi
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act and Assert
                assertEquals(3, bmiDAO.getAll().size)
                bmiDAO.delete(4)
                assertEquals(3, bmiDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing bmi in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 bmi
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act and Assert
                assertEquals(3, bmiDAO.getAll().size)
                bmiDAO.delete(bmi3.id)
                assertEquals(2, bmiDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateBmi {
        @Test
        fun ` updating existing bmis in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 bmis
                populateUserTable()
                val bmiDAO = populateBmiTable()

                //Act and Assert
                val bmi3Updated = Bmi(height = 20.0, weight = 50.0, bmivalue= 0.125, started = DateTime.now(), userId = 2, id = 3)
                bmiDAO.updateBmi(bmi3.id, bmi3Updated)
                assertEquals(bmi3Updated, bmiDAO.findByBmiId(3))
            }
        }

        @Test
        fun `updating non-existant bmi in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val bmiDAO = populateBmiTable()

                // Act & Assert
                val bmi4updated = Bmi(height = 20.0, weight = 50.0, bmivalue= 0.125, started = DateTime.now(), userId = 1, id = 3)
                bmiDAO.updateBmi(4, bmi4updated)
                assertEquals(null, bmiDAO.findByBmiId(4))
                assertEquals(3, bmiDAO.getAll().size)
            }
        }
    }
}