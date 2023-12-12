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
package knowledge;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTree {

	private final List<KnowledgeGroup> knowledgeGroups = new ArrayList<>();
	
	public KnowledgeTree(Knowledge[] knowledgeArray) {
		Knowledge[] allKnowledge = knowledgeArray;
		for(Knowledge knowledge : allKnowledge) {
			int prerequisiteCount = knowledge.getPrerequisiteCount();
			int requiredSize = prerequisiteCount + 1;
			if (knowledgeGroups.size() < requiredSize) {
				int numberOfGroupsToAdd = requiredSize - knowledgeGroups.size();
				for(int i=0; i<numberOfGroupsToAdd; i++) { knowledgeGroups.add(new KnowledgeGroup()); }
			}
			knowledgeGroups.get(prerequisiteCount).add(knowledge);
		}
		
		for(KnowledgeGroup knowledgeGroup : knowledgeGroups) {
			knowledgeGroup.sort();
		}
	}
	
	public int size() {
		return knowledgeGroups.size();
	}
	
	public Knowledge[] getKnowledge(int index) {
		return knowledgeGroups.get(index).getKnowledge();
	}
	
	private static class KnowledgeGroup {
		private final List<Knowledge> group = new ArrayList<>();
		
		public void add(Knowledge knowledge) {
			group.add(knowledge);
		}
		
		public void sort() {
			Knowledge.sortOnHoursToMaster(group);
			
		}

		public Knowledge[] getKnowledge() {
			return group.toArray(new Knowledge[0]);
		}
	}
}
