package quarano.account;

import quarano.account.Department.DepartmentIdentifier;
import quarano.account.DepartmentContact.ContactType;
import quarano.core.QuaranoRepository;
import quarano.department.CaseType;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Oliver Drotbohm
 */
public interface DepartmentRepository extends QuaranoRepository<Department, DepartmentIdentifier> {

	Optional<Department> findByName(String name);

	default Optional<DepartmentContactDto> findContactById(DepartmentIdentifier departmentId) {
		return findContactByIdAndType(departmentId, ContactType.INDEX);
	}

	default Optional<DepartmentContactDto> findContactByIdAndType(DepartmentIdentifier departmentId, CaseType caseType) {
		var contactType = CaseType.INDEX.equals(caseType) ? ContactType.INDEX
			: ContactType.CONTACT;

		return findContactByIdAndType(departmentId, contactType);
	}

	@Query("select new quarano.account.DepartmentContactDto(department.name, contact.emailAddress, contact.phoneNumber) " +
			" from Department department" +
			" join department.contacts contact on (contact.department = department and contact.type = :contactType)" +
			" where department.id = :departmentId")
	Optional<DepartmentContactDto> findContactByIdAndType(@Param("departmentId") DepartmentIdentifier departmentId, @Param("contactType") ContactType contactType);

}
