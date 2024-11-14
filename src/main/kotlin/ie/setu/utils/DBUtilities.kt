package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*

import org.jetbrains.exposed.sql.ResultRow

//designed to map a database row with a data object
//When query database, results are returned as ResultRow

//converts results from database into user object
fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    password = it[Users.password]
)
//converts results from database into activity
fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

//converts results from database into bmi
fun mapToBmi(it: ResultRow) = Bmi(
    id = it[Bmis.id],
    height = it[Bmis.height],
    weight = it[Bmis.weight],
    bmivalue = it[Bmis.bmivalue],
    started = it[Bmis.started],
    userId = it[Bmis.userId]
)

fun mapToWorkout(it: ResultRow) = Workout(
    id = it[Workouts.id],
    workout = it[Workouts.workout],
    duration = it[Workouts.duration],
    started = it[Workouts.started],
    userId = it[Workouts.userId]
)

fun mapToRecommend(it: ResultRow) = Recommend(
    id = it[Recommends.id],
    workout = it[Recommends.workout],
    recommend = it[Recommends.recommend],
    userId = it[Recommends.userId]
)