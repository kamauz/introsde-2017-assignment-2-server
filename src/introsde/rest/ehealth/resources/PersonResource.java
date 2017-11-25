package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.dao.PersonDao;
import introsde.rest.ehealth.model.Activity;
import introsde.rest.ehealth.model.Person;

import java.text.ParseException;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.json.simple.JSONObject;


@Stateless
@LocalBean
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	EntityManager entityManager;
	
	int id;
	String type;

	public PersonResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
	}
	
	public PersonResource(UriInfo uriInfo, Request request,int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}
	
	public PersonResource(UriInfo uriInfo, Request request,int id, String type) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.type = type;
	}

	
	// Application integration
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPerson() {
		Person person = this.getPersonById(id);
		if (person == null) {
			return Response.status(404).build();
		}
		return Response.ok(person).build();
	}
	
	@GET
	@Path("{activity_type}")
	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
	public Response getAllActivitiesFromUserGivenType(@PathParam("activity_type") String type) throws ParseException{
		System.out.println("Getting list of activities given user and type...");
	    List<Activity> activities = Activity.getAllFromUserGivenType(this.id,type);
	    GenericEntity entity = new GenericEntity<List<Activity>>(activities) {};
	    return Response.ok(entity).build();
	}
	
	@GET
	@Path("{activity_type}/{activity_id}")
	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
	public Response getAllActivitiesGivenId(@PathParam("activity_type") String type, @PathParam("activity_id") int activity_id) {
		System.out.println("Getting list of activities given the activity id...");
	    List<Activity> activity = Activity.getAllActivitiesGivenId(this.id, type, activity_id);
	    GenericEntity entity = new GenericEntity<List<Activity>>(activity) {};
	    return Response.ok(entity).build();
	}
	
	@PUT
	@Path("{activity_type}/{activity_id}")
	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
	public Response updateActivityType(Activity a, @PathParam("activity_type") String type, @PathParam("activity_id") int activity_id) {
		System.out.println("Updating activity type given the activity id...");
	    Activity activity = Activity.updateActivityType(this.id, a.getType().getType(), type, activity_id);
	    return Response.ok(activity).build();
	}
	
	@POST
	@Path("{activity_type}")
	@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
	public Response setNewActivityFromUserGivenType(Activity a, @PathParam("activity_type") String type) {
		System.out.println("Creating new activity given user and type...");
		EntityManager em = PersonDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        a.setType(type);
        Person person = em.find(Person.class, this.id);
        if (person==null) {
        	return null;
        }
        a.setPerson(person);
        em.persist(a);
        tx.commit();
        PersonDao.instance.closeConnections(em);
        return Response.ok(a).build();
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(Person person) {
		System.out.println("--> Updating Person... " +this.id);
		System.out.println("--> "+person.toString());
		
		Person existing = getPersonById(this.id);
		Response res;
		
		if (existing == null) {
			res = Response.noContent().build();
		} else {
			
			person.setIdPerson(this.id);
			Person updatedPerson = Person.updatePerson(person);
			res = Response.ok(updatedPerson).build();
		}

		return res;
		
	}

	@DELETE
	public void deletePerson() {
		System.out.println("Removing person");
		Person c = getPersonById(id);
		if (c == null)
			throw new RuntimeException("Delete: Person with " + id
					+ " not found");

		Person.removePerson(c);
	}

	
	public Person getPersonById(int personId) {
		System.out.println("Reading person from DB with id: "+personId);
		
		try {
			Person person = Person.getPersonById(personId);
			System.out.println("Person: "+person.toString());
			return person;
		} catch (Exception e) {
			return null;
		}
	}
	
}
