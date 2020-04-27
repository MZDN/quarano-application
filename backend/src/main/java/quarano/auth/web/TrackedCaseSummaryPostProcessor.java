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
package quarano.auth.web;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.*;

import lombok.RequiredArgsConstructor;
import quarano.auth.AccountService;
import quarano.auth.ActivationCodeService;
import quarano.department.web.TrackedCaseRepresentations;
import quarano.department.web.TrackedCaseSummary;

import java.util.function.Supplier;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link RepresentationModelProcessor} that adds a link to point to the resource issuing account registration creation
 * for cases that currently don't have an account.
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
class TrackedCaseSummaryPostProcessor implements RepresentationModelProcessor<TrackedCaseSummary> {

	private final AccountService accounts;
	private final ActivationCodeService codes;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.RepresentationModelProcessor#process(org.springframework.hateoas.RepresentationModel)
	 */
	@Override
	@SuppressWarnings("null")
	public TrackedCaseSummary process(TrackedCaseSummary model) {

		var trackedCase = model.getTrackedCase();

		Supplier<String> uri = () -> fromMethodCall(
				on(RegistrationController.class).createRegistration(trackedCase.getId(), null)).toUriString();

		// No account yet? Offer creation.
		if (accounts.findByCase(trackedCase).isEmpty()) {
			model.add(Link.of(uri.get(), TrackedCaseRepresentations.START_TRACKING));
		}

		// Pending activation code? Offer renewal.
		if (codes.getPendingActivationCode(trackedCase.getTrackedPerson().getId()).isPresent()) {
			model.add(Link.of(uri.get(), TrackedCaseRepresentations.RENEW));
		}

		return model;
	}
}
