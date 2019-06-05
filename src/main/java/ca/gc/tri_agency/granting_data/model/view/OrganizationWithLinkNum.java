package ca.gc.tri_agency.granting_data.model.view;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import ca.gc.tri_agency.granting_data.model.util.LocalizedParametersModel;
//CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `data_cabin`.`orgs_with_external_link_num` AS select `data_cabin`.`org`.`id` AS `id`,`data_cabin`.`org`.`create_date_time` AS `create_date_time`,`data_cabin`.`org`.`name_en` AS `name_en`,`data_cabin`.`org`.`name_fr` AS `name_fr`,count(`data_cabin`.`org`.`id`) AS `link_num` from (`data_cabin`.`organization` `org` join `data_cabin`.`entity_link_organization` `link` on((`data_cabin`.`org`.`id` = `data_cabin`.`link`.`org_id`))) group by `data_cabin`.`org`.`id`;

@Entity
@Table(name = "orgs_with_external_link_num", schema = "data_store")
public class OrganizationWithLinkNum implements LocalizedParametersModel {
	@Id
	private Long id;

	private String nameEn;

	private String nameFr;

	@Column(name = "link_num")
	private long linkNum;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getNameFr() {
		return nameFr;
	}

	public void setNameFr(String nameFr) {
		this.nameFr = nameFr;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Long getId() {
		return id;
	}

	public long getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(long linkNum) {
		this.linkNum = linkNum;
	}

	// @UpdateTimestamp
	// private LocalDateTime updateDateTime;

}
