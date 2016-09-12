package org.proteored.miapeapi.xml.miapeproject.merge;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.factories.MiapeDocumentFactory;
import org.proteored.miapeapi.factories.ProjectBuilder;
import org.proteored.miapeapi.interfaces.MiapeDate;
import org.proteored.miapeapi.interfaces.Project;
import org.proteored.miapeapi.interfaces.User;
import org.proteored.miapeapi.xml.merge.MergerUtil;
import org.proteored.miapeapi.xml.merge.MiapeMerger;

public class MiapeProjectMerger implements MiapeMerger<Project> {
	private static MiapeProjectMerger instance;
	private static Logger log = Logger.getLogger("log4j.logger.org.proteored");

	public static MiapeProjectMerger getInstance() {
		if (instance == null)
			instance = new MiapeProjectMerger();
		return instance;
	}

	@Override
	public Project merge(Project projectOriginal, Project projectMetadata) {
		if (projectMetadata == null)
			return projectOriginal;
		String name = null;
		String comments = null;
		MiapeDate date = null;
		User owner = null;
		Object nonNullValue;
		nonNullValue = MergerUtil.getNonNullValue(projectOriginal, projectMetadata, "getName");
		if (nonNullValue != null)
			name = (String) nonNullValue;
		nonNullValue = MergerUtil.getNonNullValue(projectOriginal, projectMetadata, "getComments");
		if (nonNullValue != null)
			comments = (String) nonNullValue;
		nonNullValue = MergerUtil.getNonNullValue(projectOriginal, projectMetadata, "getDate");
		if (nonNullValue != null)
			date = (MiapeDate) nonNullValue;
		nonNullValue = MergerUtil.getNonNullValue(projectOriginal, projectMetadata, "getOwner");
		if (nonNullValue != null)
			owner = (User) nonNullValue;
		final ProjectBuilder projectbuilder = MiapeDocumentFactory.createProjectBuilder(name)
				.comments(comments).date(date).owner(owner);
		return projectbuilder.build();
	}

}
