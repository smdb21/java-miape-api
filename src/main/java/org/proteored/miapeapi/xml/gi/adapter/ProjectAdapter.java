package org.proteored.miapeapi.xml.gi.adapter;

import org.proteored.miapeapi.interfaces.Adapter;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.xml.gi.autogenerated.MIAPEProject;
import org.proteored.miapeapi.xml.gi.autogenerated.ObjectFactory;

public class ProjectAdapter implements Adapter<MIAPEProject> {
	private final Project project;
	private final ObjectFactory factory;

	public ProjectAdapter(Project project, ObjectFactory factory) {
		this.project = project;
		this.factory = factory;

	}

	@Override
	public MIAPEProject adapt() {
		MIAPEProject projectXML = factory.createMIAPEProject();
		projectXML.setComments(project.getComments());
		projectXML.setName(project.getName());
		projectXML.setId(String.valueOf(project.getId()));
		final User owner = project.getOwner();
		if (owner != null) {
			StringBuilder sb = new StringBuilder();
			final String name = owner.getName();
			if (name != null)
				sb.append(name);
			final String lastName = owner.getLastName();
			if (lastName != null)
				sb.append(" " + lastName);
			if (!sb.toString().equals(""))
				projectXML.setOwnerName(sb.toString());
		}
		if (project.getDate() != null)
			projectXML.setDate(project.getDate().getValue());

		return projectXML;
	}

}
