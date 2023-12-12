/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import knowledge.InformationSource;
import knowledge.Knowledge;
import society.PublicKnowledge;

class PersonKnowledge implements Serializable {

	private final int[] hoursPracticed = new int[Knowledge.VALUES.length];
	private final boolean[] knowledgeKnown = new boolean[Knowledge.VALUES.length];
	
	public boolean hasKnowledge(Knowledge requiredKnowledge) {
		return knowledgeKnown[requiredKnowledge.ordinal()];
	}
	
	public List<Knowledge> getLearnableKnowledge() {
		List<Knowledge> learnableKnowledge = new ArrayList<>();
		for(Knowledge knowledge : Knowledge.VALUES) {
			if (!hasKnowledge(knowledge) && hasPrerequisiteKnowledge(knowledge)) {
				learnableKnowledge.add(knowledge);
			}
		}
		return learnableKnowledge;
	}
	
	private boolean hasPrerequisiteKnowledge(Knowledge knowledge) {
		final boolean hasPrerequisiteKnowledge;
		if (knowledge.getPrerequisiteKnowledge() != null) {
			hasPrerequisiteKnowledge = hasKnowledge(knowledge.getPrerequisiteKnowledge());
		} else {
			hasPrerequisiteKnowledge = true;
		}
		final boolean hasPrerequisiteKnowledge2;
		if (knowledge.getPrerequisiteKnowledge2() != null) {
			hasPrerequisiteKnowledge2 = hasKnowledge(knowledge.getPrerequisiteKnowledge2());
		} else {
			hasPrerequisiteKnowledge2 = true;
		}
		return hasPrerequisiteKnowledge && hasPrerequisiteKnowledge2;
	}

	public void increaseKnowledge(Knowledge knowledge, InformationSource informationSource, PublicKnowledge publicKnowledge) {
		if (knowledge == null) { throw new IllegalArgumentException("knowledge is null"); }
		int currentHoursPracticedValue = hoursPracticed[knowledge.ordinal()];
		int increment = informationSource.calculateIncrement(publicKnowledge.canObserveKnowledge(knowledge));
		int newValue = currentHoursPracticedValue + increment;
		hoursPracticed[knowledge.ordinal()] = newValue;

		if (newValue >= knowledge.getHoursRequiredToMaster()) {
			knowledgeKnown[knowledge.ordinal()] = true;
		}
	}
	
	public Knowledge getLearnableKnowledgeInProgress() {
		Knowledge knowledge = null;
		int maxHoursLearned = -1;
		for(int i=0; i<knowledgeKnown.length; i++) {
			if (!knowledgeKnown[i]) {
				int hoursPracticedValue = hoursPracticed[i];
				if (hoursPracticedValue > 0 && hoursPracticedValue > maxHoursLearned) {
					knowledge = Knowledge.VALUES[i];
					maxHoursLearned = hoursPracticedValue;
				}
			}
		}
	
		return knowledge;
	}

	public List<Knowledge> getKnownKnowledge() {
		List<Knowledge> knownKnowledgeList = new ArrayList<>();
		for(int i=0; i<knowledgeKnown.length; i++) {
			if (knowledgeKnown[i]) {
				knownKnowledgeList.add(Knowledge.VALUES[i]);
			}
		}
		return knownKnowledgeList;
	}
	
	public Map<Knowledge, String> getDescription() {
		Map<Knowledge, String> descriptionMap = new HashMap<>();
		for(int i=0; i<knowledgeKnown.length; i++) {
			int hoursPracticedValue = hoursPracticed[i];
			if (hoursPracticedValue > 0) {
				Knowledge knowledge = Knowledge.VALUES[i];
	
				descriptionMap.put(knowledge, hoursPracticedValue + " / " + knowledge.getHoursRequiredToMaster());
			}
		}
		return descriptionMap;
	}
	
	public int getKnowledgePercentageKnown(Knowledge knowledge) {
		int currentHoursPracticedValue = hoursPracticed[knowledge.ordinal()];
		int percentage = (currentHoursPracticedValue * 100) / knowledge.getHoursRequiredToMaster();
		return Math.min(100, percentage);
	}
}
