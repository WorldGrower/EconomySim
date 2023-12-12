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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgePercentageKnown {
	private final Map<Knowledge, Integer> knowledgePercentageKnownMap = new HashMap<>();
	private final Map<Knowledge, Boolean> knowledgeKnownMap = new HashMap<>();
	
	public KnowledgePercentageKnown(List<? extends KnowledgeHolder> knowledgeHolders) {
		initialize(knowledgeHolders);
	}

	private void initialize(List<? extends KnowledgeHolder> knowledgeHolders) {
		for(Knowledge knowledge : Knowledge.VALUES) {
			int percentageKnown = 0;
			for(KnowledgeHolder knowledgeHolder : knowledgeHolders) {
				percentageKnown = Math.max(percentageKnown, knowledgeHolder.getKnowledgePercentageKnown(knowledge));
			}
			knowledgePercentageKnownMap.put(knowledge, percentageKnown);
			
			boolean knowledgeKnown = false;
			for(KnowledgeHolder knowledgeHolder : knowledgeHolders) {
				if (knowledgeHolder.hasKnowledge(knowledge)) {
					knowledgeKnown = true;
				}
			}
			knowledgeKnownMap.put(knowledge, knowledgeKnown);
		}
	}
	
	public void update(List<? extends KnowledgeHolder> knowledgeHolders) {
		knowledgePercentageKnownMap.clear();
		knowledgeKnownMap.clear();
		
		initialize(knowledgeHolders);
	}
	
	public int getKnowledgePercentageKnown(Knowledge knowledge) {
		return knowledgePercentageKnownMap.get(knowledge); 
	}
	
	public boolean isKnowledgeKnown(Knowledge knowledge) {
		return knowledgeKnownMap.get(knowledge);
	}
}
