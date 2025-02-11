package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Step

import ie.setu.domain.repository.StepDAO

//main endpoints and http requests for handling API requests, handles different things
object StepController {
    private val userDao = UserDAO()
    private val stepDAO = StepDAO()

    //--------------------------------------------------------------
    // StepDAO specifics
    //-------------------------------------------------------------

    fun getAllStep(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val steps = stepDAO.getAll()
        if (steps.size != 0) {
            ctx.json(mapper.writeValueAsString( steps ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }


    fun getStepByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val steps = stepDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (steps.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(steps))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addStep(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val step = mapper.readValue<Step>(ctx.body())
        val userId = userDao.findById(step.userId)
        if (userId != null) {
            stepDAO.save(step)
            ctx.json(step)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteStepById(ctx: Context) {
        if((stepDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the step as per step id
    fun updateStep(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val stepUpdates = mapper.readValue<Step>(ctx.body())
        if((stepDAO.updateStep(id = ctx.pathParam("id").toInt(), step=stepUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }



}