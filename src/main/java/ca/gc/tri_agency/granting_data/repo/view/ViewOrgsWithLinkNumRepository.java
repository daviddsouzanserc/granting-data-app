package ca.gc.tri_agency.granting_data.repo.view;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gc.tri_agency.granting_data.model.view.OrganizationWithLinkNum;

public interface ViewOrgsWithLinkNumRepository extends JpaRepository<OrganizationWithLinkNum, Long> {

}
