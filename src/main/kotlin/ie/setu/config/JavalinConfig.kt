package ie.setu.config

import ie.setu.controllers.ActivityController
import ie.setu.controllers.BMIController
import ie.setu.controllers.UserController
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import ie.setu.utils.jsonObjectMapper

//configuring the javalin instance, server settings, custom json configuration, port setting, exception handling
class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        }
            .apply{
                exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
                error(404) { ctx -> ctx.json("404 - Not Found") }
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        //User Features
        //User Login
        app.post("/api/users/login-user", UserController::loginUser)
        //Show users
        app.get("/api/users", UserController::getAllUsers)
        //create a new user
        app.post("/api/users/create-user", UserController::addUser)
        //Delete a user
        app.delete("/api/users/delete/{user-id}", UserController::deleteUser)
        //Update a user
        app.patch("/api/users/update/{user-id}", UserController::updateUser)
        //Addons
        app.get("/api/users/id/{user-id}", UserController::getUserByUserId)
        app.get("/api/users/email/{email}", UserController::getUserByEmail)
        //-----------------------------

        //Activity Features
        app.get("/api/activities", ActivityController ::getAllActivities)
        app.post("/api/activities/add", ActivityController::addActivity)
        app.get("/api/activities/{user-id}", ActivityController:: getActivitiesByUserId)
        app.delete("/api/activities/{user-id}", ActivityController::deleteActivityById)
        app.patch("/api/activities/{id}", ActivityController::updateActivity)
        //--------------------------------

        //Bmi Features
        app.get("/api/bmi", BMIController:: getBmi)
        app.post("/api/bmi/add-bmi", BMIController::saveBmi)
        app.delete("/api/bmi/delete", BMIController::deleteBmi)
        app.patch("/api/bmi/update/{id}", BMIController::updateBmi)
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}