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
package society;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import person.CauseOfDeath;
import util.MutableInt;

public class DeathStatistics implements Serializable {

	private Map<CauseOfDeath, List<MutableInt>> stats = new EnumMap<>(CauseOfDeath.class);
	
	public DeathStatistics() {
		for(CauseOfDeath causeOfDeath : CauseOfDeath.VALUES) {
			stats.put(causeOfDeath, new ArrayList<>());
		}
	}
	
	public void addDeath(CauseOfDeath causeOfDeath) {
		List<MutableInt> causeOfDeathList = stats.get(causeOfDeath);
		if (causeOfDeathList.size() > 0) {
			causeOfDeathList.get(causeOfDeathList.size() - 1).increment();
		}
	}
	
	public void endTurn() {
		for(CauseOfDeath causeOfDeath : CauseOfDeath.VALUES) {
			stats.get(causeOfDeath).add(new MutableInt(0));
		}
	}

	public List<MutableInt> getStats(CauseOfDeath causeOfDeath) {
		return Collections.unmodifiableList(stats.get(causeOfDeath));
	}
	
	public int getTotalNumberOfDeaths(CauseOfDeath causeOfDeath) {
		int totalNumberOfDeaths = 0;
		for(MutableInt value : stats.get(causeOfDeath)) {
			totalNumberOfDeaths += value.getValue();
		}
		return totalNumberOfDeaths;
	}
}
