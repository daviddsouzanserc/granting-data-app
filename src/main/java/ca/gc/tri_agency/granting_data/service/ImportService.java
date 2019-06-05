package ca.gc.tri_agency.granting_data.service;

import java.util.Collection;

import ca.gc.tri_agency.granting_data.model.file.ProgramFromFile;

public interface ImportService {

	public void importAgencies();

	public void importProgramsFromFile();

	public Collection<ProgramFromFile> extractProgramsFromFile(String filename);

}
