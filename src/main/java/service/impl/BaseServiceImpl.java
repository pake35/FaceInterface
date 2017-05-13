package service.impl;

import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import modal.data;
import service.BaseService;
import dao.BaseDao;
@Service("baseService")
public class BaseServiceImpl implements BaseService {
	@Resource(name = "baseDao")
	private BaseDao base;
	public void create(data dt){
		base.create(dt);
	}
	
	public Vector<data> findAll(){
		return base.findAll();
	}
	
	public data getData(String url){
		return base.getData(url);
	}
}
