/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.department.activation;

import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.data.util.Streamable;

/**
 * @author Oliver Drotbohm
 */
@RequiredArgsConstructor(staticName = "of")
class ActivationCodes implements Streamable<ActivationCode> {

	private final Streamable<ActivationCode> codes;

	boolean hasRedeemedCode() {

		return codes.stream() //
				.anyMatch(ActivationCode::isRedeemed);
	}

	ActivationCodes getCancellableCodes() {

		return new ActivationCodes(codes.stream() //
				.filter(ActivationCode::isWaitingForActivation) //
				.collect(Streamable.toStreamable()));
	}

	Optional<ActivationCode> getPendingActivationCode() {

		return codes.stream() //
				.filter(ActivationCode::isWaitingForActivation) //
				.findFirst();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<ActivationCode> iterator() {
		return codes.iterator();
	}
}
