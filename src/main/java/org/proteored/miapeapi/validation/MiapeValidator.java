package org.proteored.miapeapi.validation;

import org.proteored.miapeapi.interfaces.MiapeDocument;


public interface MiapeValidator<T extends MiapeDocument> {
	ValidationReport getReport(T miape);
}
