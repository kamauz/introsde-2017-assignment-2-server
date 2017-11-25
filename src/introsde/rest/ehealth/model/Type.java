package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonValue;
@Entity  // indicates that this class is an entity to persist in DB
@XmlRootElement(name="activity_type")
@Table(name="\"Type\"") // to whate table must be persisted
@NamedQuery(name="Type.findAll", query="SELECT t FROM Type t")
public class Type implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="\"type\"")
	private String type;
	
	@OneToMany(mappedBy="activityType")
	private List<Activity> activities;

	public Type(String type) {
		this.type = type;
	}

	public Type() {

	}
	
	@XmlValue
	@JsonValue
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public static List<Type> getAll() {
		EntityManager em = PersonDao.instance.createEntityManager();
        List<Type> list = em.createNamedQuery("Type.findAll", Type.class)
            .getResultList();
        PersonDao.instance.closeConnections(em);
        return list;
	}
	
	@XmlTransient
	private List<Activity> getActivities() {
		return activities;
	}

	private void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
		
}