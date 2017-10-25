package com.ecc.hibernate_xml.service;

import java.io.Serializable;
import java.util.List;

import com.ecc.hibernate_xml.dao.Dao;
import com.ecc.hibernate_xml.assembler.Assembler;
import com.ecc.hibernate_xml.util.app.AssemblerUtils;

public abstract class AbstractService<T, R> implements Service<T, R> {
	protected final Dao<T> dao;
	protected final Assembler<T, R> assembler;

	protected AbstractService(Dao<T> dao, Assembler<T, R> assembler) {
		this.dao = dao;
		this.assembler = assembler;
	}

	@Override
	public List<R> list() {
		return AssemblerUtils.asList(dao.list(), assembler::createDTO);
	}

	@Override
	public Serializable create(R dto) throws Exception {
		return dao.create(assembler.createModel(dto));
	}

	@Override
	public void update(R dto) throws Exception {
		dao.update(assembler.createModel(dto));
	}

	@Override
	public void delete(Integer id) throws Exception {
		dao.delete(dao.get(id));
	} 

	@Override
	public R get(Integer id) throws Exception {
		return assembler.createDTO(dao.get(id));
	}
}