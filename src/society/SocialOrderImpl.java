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

import person.Person;

class SocialOrderImpl implements Serializable {
	
	private static final int DEFAULT_THIEF_PENALTY = 5;
	
	private final TheftStatistics theftStatistics = new TheftStatistics();
	
	private int currentNumberOfMaintains = 0;
	private int baseThiefPenalty = DEFAULT_THIEF_PENALTY;
	
	public void registerTheft(Person person, Person targetPerson, boolean success, boolean detected) {
		theftStatistics.registerTheft(person, targetPerson, success, detected);
	}

	public int calculateKnownThiefPenalty(Person person, int populationSize) {
		return theftStatistics.isKnownThief(person) ? (int)(baseThiefPenalty + 15 / Math.log(populationSize)) : 0;
	}

	public void maintainSocialOrder() {
		currentNumberOfMaintains++;
	}
	
	public void startTurn() {
		currentNumberOfMaintains = 0;
		theftStatistics.startTurn();
	}
	
	public void endTurn(int populationSize) {
			int neededNumberOfMaintains = populationSize / 10;
			if (neededNumberOfMaintains > 0) {
				int maintainPercentage = (currentNumberOfMaintains * 100) / neededNumberOfMaintains;
				baseThiefPenalty = 3 + (maintainPercentage / 50);
			} else {
				baseThiefPenalty = DEFAULT_THIEF_PENALTY;
			}
	}

	public boolean isKnownThief(Person person) {
		return theftStatistics.isKnownThief(person);
	}

	public void onDeath(Person person) {
		theftStatistics.onDeath(person);
	}

	public String getBaseThiefPenaltyDescription() {
		return Integer.toString(baseThiefPenalty);
	}

	public int getLatestNumberOfThefts() {
		return theftStatistics.getLatestNumberOfThefts();
	}
}
