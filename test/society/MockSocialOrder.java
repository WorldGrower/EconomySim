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

import java.util.List;

import action.TradeArgs;
import person.Person;

public class MockSocialOrder implements SocialOrder {

	@Override
	public void registerTheft(Person person, Person targetPerson, boolean success, boolean detected) {
	}

	@Override
	public int calculateKnownThiefPenalty(Person person) {
		return 0;
	}

	@Override
	public void punishThief(Person thief, Person victim, TradeArgs tradeArgs, boolean success) {

	}

	@Override
	public void maintainSocialOrder() {

	}

	@Override
	public void createCalendar() {
	}

	@Override
	public boolean calendarExists() {
		return false;
	}

	@Override
	public void addPerformedTrade(PerformedTrade performedTrade) {
	}

	@Override
	public List<PerformedTrade> getPerformedTrades() {
		return null;
	}
}
