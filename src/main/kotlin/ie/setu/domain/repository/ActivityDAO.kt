package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//activity data access objects that handles database operations
class ActivityDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<Activity> {
        val activitiesList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map { activitiesList.add(mapToActivity(it)) }
        }
        return activitiesList
    }

    //Find a specific activity by activity id
    fun findByActivityId(id: Int): Activity?{
        return transaction {
            Activities.selectAll().where { Activities.id eq id}.map{mapToActivity(it)}.firstOrNull()
        }
    }

    //Find all activities for a specific user id
    fun findByUserId(userId: Int): List<Activity>{
        return transaction {
            Activities.selectAll().where {Activities.userId eq userId}.map {mapToActivity(it)}
        }
    }

    //Save an activity to the database
    fun save(activity: Activity){
        transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userId] = activity.userId
            }
        }
    }

    //delete by user id of an activity from database
    fun delete(id: Int): Int {
        return transaction {
            Activities.deleteWhere { Activities.id eq id}
        }
    }

    //update and activity in the database with activity id
    fun updateActivity(id: Int, activity: Activity): Int{
        return transaction {
            Activities.update({ Activities.id eq id }) {
                it[description] = activity.description
                it[duration] = activity.duration
                it[started] = activity.started
                it[calories] = activity.calories
                it[userId] = activity.userId
            }
        }
    }

}