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
package quarano.tracking.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import lombok.RequiredArgsConstructor;
import quarano.QuaranoWebIntegrationTest;
import quarano.WithQuaranoUser;
import quarano.tracking.DiaryEntryRepository;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.web.DiaryRepresentations.DiaryEntryInput;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * @author Oliver Drotbohm
 */
@QuaranoWebIntegrationTest
@RequiredArgsConstructor
class DiaryControllerWebIntegrationTests {

	private final MockMvc mvc;
	private final DiaryEntryRepository entries;
	private final ObjectMapper jackson;

	@Test
	@WithQuaranoUser("test3")
	void updatesDiaryEntry() throws Exception {

		var entry = entries.findByTrackedPersonId(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2) //
				.iterator().next();
		var slot = entry.getSlot();

		DiaryEntryInput payload = new DiaryEntryInput(slot) //
				.setBodyTemperature(42.0f);

		String response = mvc.perform(put("/api/diary/{identifier}", entry.getId()) //
				.content(jackson.writeValueAsString(payload)) //
				.contentType(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isOk()) //
				.andReturn().getResponse().getContentAsString();

		var document = JsonPath.parse(response);

		assertThat(document.read("$.id", String.class)).isEqualTo(entry.getId().toString());
		assertThat(document.read("$.slot.timeOfDay", String.class)).isEqualToIgnoringCase(slot.getTimeOfDay().toString());
		assertThat(document.read("$.slot.date", String.class)).isEqualTo(slot.getDate().toString());
		assertThat(document.read("$.bodyTemperature", float.class)).isEqualTo(42.0f);
	}
}
