package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity  // indicates that this class is an entity to persist in DB
@XmlRootElement(name="activity")
@Table(name="\"Activity\"") // to whate table must be persisted
@NamedQueries({
	@NamedQuery(name="Activity.getAllFromUserGivenType",
			query="SELECT a FROM Activity a WHERE a.person=:Person AND a.activityType=:Type"),

	@NamedQuery(name="Activity.getAllActivitiesGivenId",
			query="SELECT a FROM Activity a WHERE a.person=:Person AND a.activityType=:Type AND a=:Activity"),
	
	@NamedQuery(name="Activity.filterForDates",
			query="SELECT a FROM Activity a WHERE a.person=:Person AND a.startdate > :Before AND a.startdate < :After")
})
public class Activity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(strategy=GenerationType.AUTO) 
    @Column(name="\"idActivity\"") // maps the following attribute to a column
    private int idActivity;
	@Column(name="\"name\"")
	private String name; // in kg
	@Column(name="\"description\"")
	private String description; // in m
	@ManyToOne
	@JoinColumn(name="\"activityType\"", referencedColumnName="\"type\"", insertable=true, updatable=true)
	private Type activityType;
	@Column(name="\"place\"")
	private String place;
	@Column(name="\"startdate\"")
	private String startdate;
	@ManyToOne
	@JoinColumn(name="\"idPerson\"",referencedColumnName="\"idPerson\"")
	private Person person;

	public Activity(String name, String description, String place, String type, String startdate) {
		this.name = name;
		this.description = description;
		this.place = place;
		this.setType(type);
		this.startdate = startdate;
	}

	public Activity() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	public Type getType() {
		return this.activityType;
	}

	public void setType(String type) {
		
		EntityManager em = PersonDao.instance.createEntityManager();
		Type t = em.find(Type.class, type);
    	if (t == null) {
    		EntityTransaction tx = em.getTransaction();
            tx.begin();
            t = new Type(type);
            em.persist(t);
            tx.commit();
    	}
    	PersonDao.instance.closeConnections(em);
    	this.activityType = t;
	}
	
	@XmlTransient
	public Person getPerson() {
	    return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public static List<Activity> getAllFromUserGivenType(int id, String type) {
		EntityManager em = PersonDao.instance.createEntityManager();
		Person p = em.find(Person.class, id);
		Type t = em.find(Type.class, type);
		
        List<Activity> list = em.createNamedQuery("Activity.getAllFromUserGivenType", Activity.class)
        	.setParameter("Person", p)
        	.setParameter("Type", t)
            .getResultList();
        PersonDao.instance.closeConnections(em);
        return list;
        
	}
	
	public static List<Activity> filterForDates(int id, String type, String before, String after) throws ParseException {		
        /*try {
        	EntityManager em = PersonDao.instance.createEntityManager();
    		Person p = em.find(Person.class, id);
    		Type t = em.find(Type.class, type);
    		

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Date beforeDate = formatter.parse(before);
            Date afterDate = formatter.parse(after);
	        List<Activity> list = em.createNamedQuery("Activity.filterForDates", Activity.class)
	        	.setParameter("Person", p)
	        	.setParameter("Type", t)
	        	.setParameter("Before", beforeDate)
	        	.setParameter("After", afterDate)
	            .getResultList();
	        PersonDao.instance.closeConnections(em);
	        return list;
        } catch(Exception e) {
        	e.printStackTrace();
        	return null;
        }*/
		return null;
        
        

	}
	
	public static Activity updateActivityType(int id, String newType, String type, int activity_id) {
		EntityManager em = PersonDao.instance.createEntityManager();
		Person p = em.find(Person.class, id);
		Type t = em.find(Type.class, type);
		Activity a = em.find(Activity.class, activity_id);
		
		Activity element = em.createNamedQuery("Activity.getAllActivitiesGivenId", Activity.class)
	        	.setParameter("Person", p)
	        	.setParameter("Type", t)
	        	.setParameter("Activity", a)
	            .getSingleResult();
	    
		EntityTransaction tx = em.getTransaction();
        tx.begin();
        element.setType(newType);
		tx.commit();
		
		em.persist(element);
		
		PersonDao.instance.closeConnections(em);
		return element;
	}
	
	public static List<Activity> getAllActivitiesGivenId(int id, String type, int act_id) {
		try {
			EntityManager em = PersonDao.instance.createEntityManager();
			System.out.println(id+" "+type+" "+act_id);
			Person p = em.find(Person.class, id);
			Type t = em.find(Type.class, type);
			Activity a = em.find(Activity.class, act_id);
			
	        List<Activity> element = em.createNamedQuery("Activity.getAllActivitiesGivenId", Activity.class)
	        	.setParameter("Person", p)
	        	.setParameter("Type", t)
	        	.setParameter("Activity", a)
	            .getResultList();
	        
	        PersonDao.instance.closeConnections(em);
	        return element;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        
	}
	
	@XmlAttribute(name="id")
	private int getIdActivity() {
		return idActivity;
	}
		
}