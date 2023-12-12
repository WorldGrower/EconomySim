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

import person.CauseOfDeath;
import person.Person;

public enum TheftPunishment {
	DEATH_FOR_KNOWN_THIEF("death for known thief") {
		
		@Override
		public void perform(Person thief, boolean isKnownThief) {
			if (isKnownThief) {
				thief.kill(CauseOfDeath.RETRIBUTION);
			}
		}
	},
	DEATH("death") {

		@Override
		public void perform(Person thief, boolean isKnownThief) {
			thief.kill(CauseOfDeath.RETRIBUTION);
		}
	}
	;
	
	public static final TheftPunishment[] VALUES = values();

	private final String description;

	private TheftPunishment(String description) {
		this.description = description;
	}

	public abstract void perform(Person thief, boolean isKnownThief);
	
	@Override
	public String toString() {
		return description;
	}
}
