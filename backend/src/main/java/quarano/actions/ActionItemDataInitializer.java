package quarano.actions;

import lombok.RequiredArgsConstructor;
import quarano.actions.ActionItem.ItemType;
import quarano.core.DataInitializer;
import quarano.tracking.BodyTemperature;
import quarano.tracking.DiaryEntryRepository;
import quarano.tracking.Slot;
import quarano.tracking.TrackedPerson;
import quarano.tracking.TrackedPersonDataInitializer;
import quarano.tracking.TrackedPersonRepository;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Order(800)
@Component
@RequiredArgsConstructor
public class ActionItemDataInitializer implements DataInitializer {

	private final TrackedPersonRepository people;
	private final DiaryEntryRepository entries;
	private final ActionItemRepository items;
	private final AnomaliesProperties config;

	/*
	 * (non-Javadoc)
	 * @see quarano.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		TrackedPerson sandra = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON3_ID_DEP2).orElseThrow();
		TrackedPerson gustav = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON4_ID_DEP1).orElseThrow();
		TrackedPerson nadine = people.findById(TrackedPersonDataInitializer.VALID_TRACKED_PERSON5_ID_DEP1).orElseThrow();

		items.save(new DiaryEntryMissingActionItem(sandra.getId(), Slot.now().previous()));
		items.save(new DiaryEntryActionItem(gustav.getId(), entries.findByTrackedPerson(gustav).iterator().next(),
				ItemType.MEDICAL_INCIDENT,
				Description.forIncreasedTemperature(BodyTemperature.of(40.1f), config.getTemperatureThreshold())));
		items.save(new DiaryEntryActionItem(nadine.getId(), entries.findByTrackedPerson(nadine).iterator().next(),
				ItemType.MEDICAL_INCIDENT,
				Description.forIncreasedTemperature(BodyTemperature.of(40.1f), config.getTemperatureThreshold())));
	}
}
