package quarano.account;

import static quarano.account.DepartmentContact.*;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import quarano.account.Department.DepartmentIdentifier;
import quarano.core.QuaranoAggregate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.MapKey;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "departments")
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Department extends QuaranoAggregate<Department, DepartmentIdentifier> {

	private @Getter @Column(unique = true) String name;
	@MapKeyEnumerated(value = EnumType.STRING)
	@MapKey(name = "type")
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
	private @Getter @Setter Map<ContactType, DepartmentContact> contacts = new HashMap<>();

	public Department(String name) {
		this(name, UUID.randomUUID());
	}

	public Department(String name, UUID uuid) {
		this(name, DepartmentIdentifier.of(uuid));
	}

	Department addDepartmentContacts(List<DepartmentContact> departmentContacts) {
		departmentContacts.stream()
				.peek(departmentContact -> departmentContact.setDepartment(this))
				.forEach(departmentContact -> this.contacts.put(departmentContact.getType(), departmentContact));
		return this;
	}

	// fixed Id department for tests and integration data
	Department(String name, DepartmentIdentifier fixedId) {

		this.id = fixedId;
		this.name = name;
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class DepartmentIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 7871473225101042167L;

		final UUID departmentId;

		@Override
		public String toString() {
			return departmentId.toString();
		}
	}

}
