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
import java.util.List;

public class TradeOverviewImpl implements TradeOverview, Serializable {

	private final List<PerformedTrade> performedTrades = new ArrayList<>();
	
	@Override
	public void addPerformedTrade(PerformedTrade performedTrade) {
		performedTrades.add(performedTrade);
	}

	public void endTurn() {
		performedTrades.clear();
	}

	@Override
	public List<PerformedTrade> getPerformedTrades() {
		return Collections.unmodifiableList(performedTrades);
	}
}