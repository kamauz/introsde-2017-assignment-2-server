package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;

@Entity  // indicates that this class is an entity to persist in DB
@XmlRootElement
@Table(name="\"Person\"") // to whate table must be persisted
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @Column(name="\"idPerson\"")
    @GeneratedValue(strategy=GenerationType.AUTO) 
    private int idPerson;
    @Column(name="\"lastname\"")
    private String lastname;
    @Column(name="\"name\"")
    private String name;
    @Column(name="\"username\"")
    private String username;
    @Column(name="\"birthdate\"")
    private String birthdate; 
    @Column(name="\"email\"")
    private String email;
    // mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<Activity> activity;
	
    public void setIdPerson(int id) {
    	this.idPerson = id;
    }
    
    @XmlAttribute(name="id")
    public int getIdPerson() {
    	return this.idPerson;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    @XmlElement(name="firstname")
    public String getName() {
    	return this.name;
    }
    
    public void setLastname(String lastname) {
    	this.lastname = lastname;
    }
    
    public String getLastname() {
    	return this.lastname;
    }
    
    public void setBirthdate(String birthdate) {
    	this.birthdate = birthdate;
    }
    
    public String getBirthdate() {
    	return this.birthdate;
    }
    
    @XmlElementWrapper(name="activities")
    @XmlElement(name="activities")
	public List<Activity> getActivity() {
		return activity;
	}

	public void setActivity(List<Activity> activity) {
		this.activity = activity;
	}
    
    public static Person getPersonById(int personId) {
    	
		EntityManager em = PersonDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        PersonDao.instance.closeConnections(em);
        return p;
    }

    public static List<Person> getAll() {
    	
        EntityManager em = PersonDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        PersonDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
    	
        EntityManager em = PersonDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        if (p.getActivity()!=null) {
	        for(Activity activity : p.getActivity()) {
	        	activity.setPerson(p);
	        }
        }
        
        em.persist(p);
        tx.commit();
        PersonDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = PersonDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        PersonDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
    	
        EntityManager em = PersonDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p); //
        em.remove(p);
        tx.commit();
        PersonDao.instance.closeConnections(em);
    }

    // add below all the getters and setters of all the private attributes
}