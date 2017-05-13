package dao.impl;

import java.util.List;
import java.util.Vector;

import modal.data;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import params.Img;
import dao.BaseDao;

public class BaseDaoImpl implements BaseDao{
	
	@Autowired
	SessionFactory sessionFactory;
	
	private Session getCurrentSession(){
		return this.sessionFactory.getCurrentSession();
	}

	public void create(modal.data dt) {
		// TODO Auto-generated method stub
		this.getCurrentSession().save(dt);
	}

	public Vector<data> findAll() {
		Query query = this.getCurrentSession().createSQLQuery("SELECT value FROM face_data");
		return (Vector<data>) query.list();
	}
	
	public data getData(String url){
		return (data) this.sessionFactory.getCurrentSession().get(data.class, url);
	}
	
}
