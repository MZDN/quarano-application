package quarano.account;

import static org.assertj.core.api.Assertions.*;
import static quarano.account.DepartmentDataInitializer.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoIntegrationTest;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;
import quarano.department.CaseType;

import org.junit.jupiter.api.Test;

@QuaranoIntegrationTest
@RequiredArgsConstructor
class DepartmentRepositoryIntegrationTest {
	private final DepartmentRepository departments;

	@Test
	void testFindContactByIdAndTypeForIndexType() {
		var contact = departments.findContactByIdAndType(DEPARTMENT_ID_DEP2, CaseType.INDEX);

		assertThat(contact).isPresent();
		assertThat(contact).hasValue(new DepartmentContactDto("GA Darmstadt", EmailAddress.of("index-email@gadarmstadt.de"), PhoneNumber.of("0123456789")));
	}

	@Test
	void testFindContactByIdAndTypeForContactTypes() {
		for (CaseType caseType : new CaseType[] {CaseType.CONTACT, CaseType.CONTACT_MEDICAL, CaseType.CONTACT_VULNERABLE}) {
			var contact = departments.findContactByIdAndType(DEPARTMENT_ID_DEP2, caseType);

			assertThat(contact).isPresent();
			assertThat(contact).hasValue(new DepartmentContactDto("GA Darmstadt", EmailAddress.of("contact-email@gadarmstadt.de"), PhoneNumber.of("00123456789")));
		}
	}
}
