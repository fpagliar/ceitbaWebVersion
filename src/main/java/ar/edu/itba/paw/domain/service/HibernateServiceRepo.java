package ar.edu.itba.paw.domain.service;

import java.util.ArrayList;
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
		if (duplicatedData("name", service.getName())) {
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
		return getByType(Service.Type.SPORT);
	}
	
	@Override
	public List<Service> getCourses(){
		return getByType(Service.Type.COURSE);
	}

	@Override
	public List<Service> getOthers(){
		return getByType(Service.Type.OTHER);
	}

	@Override
	public List<Service> getLockers(){
		return getByType(Service.Type.LOCKER);
	}

	private List<Service> getByType(Service.Type type){
		return find("from Service where type = ?", type);
	}
	
	@Override
	public List<Service> getActive(){
		return find("from Service where status = 'ACTIVE'");
	}

	@Override
	public List<Service> getInactive(){
		return find("from Service where status = 'INACTIVE'");
	}

	@Override
	public List<Service> search(String s) {
		List<Service> ans = new ArrayList<Service>();
		List<Service> all = find("from Service where name like ?", "%"+ s + "%");
		ans.addAll(all);
		return ans;
	}
	
	private boolean duplicatedData(String field, String value) {
		return !find("from Service where ? = ?", field, value).isEmpty();
	}
}