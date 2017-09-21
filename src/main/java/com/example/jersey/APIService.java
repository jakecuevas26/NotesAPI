package com.example.jersey;

import org.codehaus.jettison.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/notes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class APIService {
    @POST
    public JSONObject createNoteAPI( JSONObject notes ) throws Exception {
        String input = (String) notes.get("body");
        return DatabaseService.createNote( input );
    }

    @GET
    @Path("/{id}")
    public JSONObject getNoteAPI(@PathParam("id")int id) throws Exception {
        return DatabaseService.getNote( id );
    }

    @GET
    public JSONObject getAllNotesAPI(@QueryParam("query")String query) throws Exception {
        return DatabaseService.getAllNotes( query );
    }
}
