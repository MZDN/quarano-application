package quarano.account;

import lombok.EqualsAndHashCode;
import lombok.Value;
import quarano.core.EmailAddress;
import quarano.core.PhoneNumber;

@EqualsAndHashCode
@Value
public class DepartmentContactDto {
	String name;
	EmailAddress email;
	PhoneNumber phoneNumber;
}
