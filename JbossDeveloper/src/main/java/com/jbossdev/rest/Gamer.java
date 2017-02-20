package com.jbossdev.rest;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.jbossdev.beans.Game;
import com.jbossdev.beans.GameManager;

@Path("/gamer")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
public class Gamer {

	@Inject
	private GameManager gameManager;
	
	@Path("/games")
	@GET
	public Response getListOfGames() {
		if (gameManager == null) {
			Response response = Response.status(Status.INTERNAL_SERVER_ERROR).header("NOTE", "Problem in dependency injection").build();
			return response;
		}
		Collection<Game> availableGames = gameManager.getListAvailableGames();
		return Response.status(Status.OK).entity(availableGames).build();
	}
	
	@Path("/games/{name}")
	@Produces(MediaType.APPLICATION_XML)
	@GET
	public Response getGame(@PathParam("name") String name) {
		if (name == null || name.isEmpty()) {
			Response response = Response.status(Status.BAD_REQUEST).
					header("NOTE", "Input name must be not null and not empty string").build();
			return response;
		}
		Game gameLookup = gameManager.findGameByName(name);
		Response response = null;
		if (gameLookup == null) {
			response = Response.status(Status.NOT_FOUND).build();
		} else {
			response = Response.status(Status.OK).entity(gameLookup).build();
		}
		return response;
	}
	
	@Path("/games")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@POST
	public Response addGame(@FormParam("name") String name, @FormParam("rules") String rules) {
		if (name == null || name.isEmpty() || rules == null || rules.isEmpty()) {
			Response response = Response.status(Status.BAD_REQUEST).
					header("NOTE", "Input name and rules must be not null and not empty string").build();
			return response;
		}
		Game oldGame = gameManager.findGameByName(name);
		if (oldGame != null) {
			return Response.status(Status.BAD_REQUEST).header("NOTE", "Already exists").entity(oldGame).build();
		}

		Game newGame = gameManager.addGame(name, rules);
		return Response.status(Status.CREATED).entity(newGame).build();
	}
	
	@Path("/games/{name}")
	@Produces(MediaType.APPLICATION_XML)
	@DELETE
	public Response deleteGame(@PathParam("name") String name) {
		if (name == null || name.isEmpty()) {
			Response response = Response.status(Status.BAD_REQUEST).
					header("NOTE", "Input name must be not null and not empty string").build();
			return response;
		}
		Game gameLookup = gameManager.deleteGame(name);
		Response response = null;
		if (gameLookup == null) {
			response = Response.status(Status.NOT_FOUND).build();
		} else {
			response = Response.status(Status.OK).entity(gameLookup).build();
		}
		return response;
	}
}
