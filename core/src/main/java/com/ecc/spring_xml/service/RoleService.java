package com.ecc.spring_xml.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import com.ecc.spring_xml.dto.RoleDTO;
import com.ecc.spring_xml.model.Role;
import com.ecc.spring_xml.dao.RoleDao;
import com.ecc.spring_xml.assembler.RoleAssembler;
import com.ecc.spring_xml.util.app.AssemblerUtils;
import com.ecc.spring_xml.util.ValidationUtils;

public class RoleService extends AbstractService<Role, RoleDTO> implements Validator {
	private static final Integer MAX_CHARACTERS = 20;

	private final RoleDao roleDao;
	private final RoleAssembler roleAssembler;

	public RoleService(RoleDao roleDao, RoleAssembler roleAssembler) {
		super(roleDao, roleAssembler);
		this.roleDao = roleDao;
		this.roleAssembler = roleAssembler;
	}

	@Override
	public boolean supports(Class clazz) {
        return clazz.isAssignableFrom(RoleDTO.class);
    }

    @Override
    public void validate(Object command, Errors errors) {
    	RoleDTO role = (RoleDTO) command;
		ValidationUtils.testNotEmpty(role.getName(), "name", errors, "localize:role.form.label.name");
		ValidationUtils.testMaxLength(role.getName(), "name", errors, MAX_CHARACTERS, "localize:role.form.label.name");
    }

	public List<RoleDTO> list() {
		return AssemblerUtils.asList(roleDao.list(), roleAssembler::createDTO);
	}

	@Override
	protected RuntimeException onCreateFailure(Role role, RuntimeException cause) {
		return onUpdateFailure(role, cause);
	}

	@Override
	protected RuntimeException onUpdateFailure(Role role, RuntimeException cause) {
		if (cause instanceof DataIntegrityViolationException) {
			return new RuntimeException(String.format(
				"Role name \"%s\" is already existing.", role.getName()));
		}
		return super.onUpdateFailure(role, cause);
	}

	@Override
	protected RuntimeException onDeleteFailure(Role role, RuntimeException cause) {
		if (cause instanceof DataIntegrityViolationException && role.getPersons().size() > 0) {
			String personNames = role.getPersons()
				.stream()
				.map(person -> person.getName().toString())
				.collect(Collectors.joining("; "));

			return new RuntimeException(
				String.format("Role is in used by persons [%s].", personNames));
		}
		return super.onDeleteFailure(role, cause);
	}
}