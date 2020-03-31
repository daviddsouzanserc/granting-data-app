INSERT INTO granting_system (id, acronym, name_en, name_fr) VALUES (select next value for seq_granting_system,'RP1','Research Portal','Portail de Recherche');
INSERT INTO granting_system (id, acronym, name_en, name_fr) VALUES (seq_granting_system.nextval,'CRM','CRM','CRM'),(seq_granting_system.nextval,'SSHRC Online','SSHRC Online','SSHRC Online'),(seq_granting_system.nextval,'AMIS','AMIS','AMIS'),(seq_granting_system.nextval,'NSERC Online','NSERC Online','NSERC Online'),(seq_granting_system.nextval,'NAMIS','NAMIS','NAMIS');
INSERT INTO agency VALUES (seq_agency.nextval, 'SSHRC', 'CRSH', 'Social Sciences and Humanities Research Council', 'Conseil de recherches en sciences humaines'), (seq_agency.nextval, 'NSERC', 'CRSNG', 'Natural Sciences and Engineering Research Council ', 'Conseil de recherches en sciences naturelles et en génie'), (seq_agency.nextval, 'CIHR', 'IRSC', 'Canadian Institutes of Health Research ', 'Instituts de recherche en santé');
INSERT INTO granting_stage (id, name_en, name_fr) VALUES (seq_granting_stage.nextval,'ADMIN','');
INSERT INTO granting_stage (id, name_en, name_fr) VALUES (seq_granting_stage.nextval,'APPLY','');
INSERT INTO granting_stage (id, name_en, name_fr) VALUES (seq_granting_stage.nextval,'ASSESS','');
INSERT INTO granting_stage (id, name_en, name_fr) VALUES (seq_granting_stage.nextval,'AWARD','');
INSERT INTO granting_stage (id, name_en, name_fr) VALUES (seq_granting_stage.nextval,'AQUIT','');
INSERT INTO fiscal_year VALUES (seq_fiscal_year.nextval,2017), (seq_fiscal_year.nextval,2018), (seq_fiscal_year.nextval,2019);