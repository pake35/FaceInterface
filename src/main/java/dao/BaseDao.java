package dao;

import java.util.Vector;

import modal.data;

public interface BaseDao {
	
	public void create(data dt);
	
	public data getData(String url);
	
	public Vector<data> findAll();
}
