package ie.setu.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.nonExistingEmail
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

//retrieving some test data from Fixtures
val user1 = users[0]
val user2 = users[1]
val user3 = users[2]

class UserDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadUsers {
        @Test
        fun `getting all users from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act and Assert
                assertEquals(3, userDAO.getAll().size)
            }
        }

//        @Test
//        fun `get user by email and password with exits`(){
//            transaction {
//                val userDao=  populateUserTable()
//
//                assertEquals(true, userDao.loginUser(UserLogin("justin@setu.com", "pass123")))
//            }
//        }


        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(null, userDAO.findById(4))
            }
        }
        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user3, userDAO.findById(user3.id))
            }
        }

        @Test
        fun `get all users over empty table return null`(){
            transaction {
                //Arrange - create and set up userDAO object
                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                //Act and assert
                assertEquals(0, userDAO.getAll().size)
            }
        }

        @Test
        fun `get all user by email that doesn't exist, results in no user returned`(){
            transaction {

                //Arrange - create a table of 3 users
                val userDAO = populateUserTable()

                //Act and assert
                assertEquals(null, userDAO.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(user2, userDAO.findByEmail(user2.email))
            }
        }
    }

    @Nested
    inner class CreateUsers {
        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three users
                val userDAO = populateUserTable()

                //Act & Assert
                assertEquals(3, userDAO.getAll().size)
                assertEquals(user1, userDAO.findById(user1.id))
                assertEquals(user2, userDAO.findById(user2.id))
                assertEquals(user3, userDAO.findById(user3.id))
            }
        }
    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 users
                val userDAO = populateUserTable()

                //Act and Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(4)
                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 users
                val userDAO = populateUserTable()

                //Act and Assert
                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(user3.id)
                assertEquals(2, userDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun ` updating existing users in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 users
                val userDAO = populateUserTable()

                //Act and Assert
                val user3Updated = User(3, "new username", "new@mail.ie", "password")
                userDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated, userDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existant users in table results in no updation`(){
            transaction {

                //Arrange - creating and population of table with 3 users
                val userDAO = populateUserTable()

                //Act and assert
                val user4Updated = User(4, "new name", "newmail.ie", "password")
                userDAO.update(4, user4Updated)
                assertEquals(null, userDAO.findById(4))
                assertEquals(3, userDAO.getAll().size)
            }
        }
    }

}


