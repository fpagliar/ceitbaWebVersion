package ar.edu.itba.paw.domain.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.domain.AbstractHibernateRepo;
import ar.edu.itba.paw.domain.DuplicatedDataException;

@Repository
public class HibernateServiceRepo extends AbstractHibernateRepo implements ServiceRepo{

	@Autowired
	public HibernateServiceRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(Service service) {
		if (duplicatedData("name", "" + service.getName())) {
			throw new DuplicatedDataException(service);
		}
		save(service);
	}

	@Override
	public Service get(int id) {
		return get(Service.class, id);
	}
	
	@Override
	public Service get(String name){
		List<Service> services = find("from Service where name = ?", name);
		if(services.size() == 0)
			return null;
		if(services.size() == 1)
			return services.get(0);
		throw new RuntimeException("An internal error occurred");
	}

	@Override
	public List<Service> getAll() {
		return find("from Service");
	}
	
	@Override
	public List<Service> getSports(){
		return getByType("SPORT");
	}
	
	@Override
	public List<Service> getCourses(){
		return getByType("COURSE");
	}

	@Override
	public List<Service> getOthers(){
		return getByType("OTHER");
	}

	private List<Service> getByType(String type){
		return find("from Service where type = ?", type);		
	}
	
	private boolean duplicatedData(String field, String value) {
		return !find("from Service where ? = ?", field, value).isEmpty();
	}
}