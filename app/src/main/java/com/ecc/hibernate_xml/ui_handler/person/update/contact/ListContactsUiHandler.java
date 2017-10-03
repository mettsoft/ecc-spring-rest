package com.ecc.hibernate_xml.ui_handler.person.update.contact;

import java.util.stream.Collectors;
import com.ecc.hibernate_xml.ui_handler.CompositeUiHandler;
import com.ecc.hibernate_xml.ui_handler.UiHandler;
import com.ecc.hibernate_xml.util.InputHandler;
import com.ecc.hibernate_xml.service.ContactService;
import com.ecc.hibernate_xml.model.Person;
import com.ecc.hibernate_xml.model.Contact;

public class ListContactsUiHandler extends UiHandler {

	private ContactService contactService;
	private Person person;

	public ListContactsUiHandler(String operationName, Person person) {
		super(operationName);
		this.contactService = new ContactService();
		this.person = person;
	}

	@Override 
	public void onHandle() throws Exception {
		System.out.println(String.format("Person \"%s\" has the following contacts:", 
			person.getName()));
		contactService.listContacts(person).stream()
			.map(contact -> contact.toString())
			.forEach(System.out::println);
	}

	@Override 
	protected Boolean relinquishControl() {
		return true;
	}
}