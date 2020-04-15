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
package quarano.department;

import lombok.EqualsAndHashCode;
import quarano.core.QuaranoAggregate;
import quarano.department.Task.TaskIdentifier;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.jddd.core.types.Identifier;

/**
 * @author Oliver Drotbohm
 */
public class Task extends QuaranoAggregate<Task, TaskIdentifier> {

	@Embeddable
	@EqualsAndHashCode
	static class TaskIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = 1253942895031412367L;
		UUID id = UUID.randomUUID();
	}
}
