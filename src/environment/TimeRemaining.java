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
package environment;

import java.io.Serializable;

public class TimeRemaining implements Serializable {
	private static final int AVAILABLE_DAY_TIME = 12;
	private static final int AVAILABLE_NIGHT_TIME = 6;
	
	private final LightSourceEnvironment lightSourceEnvironment;
	
	private int dayTimeRemaining = AVAILABLE_DAY_TIME;
	private int nightTimeRemaining = AVAILABLE_NIGHT_TIME;

	public TimeRemaining(LightSourceEnvironment lightSourceEnvironment) {
		this.lightSourceEnvironment = lightSourceEnvironment;
	}
	
	public boolean hasRemainingTime(int time) {
		if (dayTimeRemaining >= time) {
			return true;
		} else {
			return get() >= time;
		}
	}
	
	public int get() {
		if (lightSourceEnvironment.isLightedAtNight()) {
			return dayTimeRemaining + nightTimeRemaining;
		} else {
			return dayTimeRemaining;
		}
	}

	public void use(int timeRequired) {
		while (dayTimeRemaining > 0 && timeRequired > 0) {
			dayTimeRemaining--;
			timeRequired--;
		}
		if (timeRequired > 0) {
			if (lightSourceEnvironment.isLightedAtNight()) {
				while (nightTimeRemaining > 0 && timeRequired > 0) {
					nightTimeRemaining--;
					timeRequired--;
				}
			}
		}
		if (timeRequired > 0) {
			throw new IllegalStateException("timeRequired is not zero: " + timeRequired);
		}
	}
	
	public void reset(CalendarSystem calendarSystem, boolean hasCalendarKnowledge) {
		// without a calendar system it's harder to plan tasks; predict weather; know when to plant crops; when to look for particular animals 
		dayTimeRemaining = AVAILABLE_DAY_TIME - ((calendarSystem.calendarExists() && hasCalendarKnowledge) ? 0 : 1);
		nightTimeRemaining = AVAILABLE_NIGHT_TIME;
	}
	
	@Override
	public String toString() {
		return dayTimeRemaining + " / " + AVAILABLE_DAY_TIME + ", " + nightTimeRemaining + " / " + AVAILABLE_NIGHT_TIME;
	}

	public TimeRemaining copy() {
		return new TimeRemaining(lightSourceEnvironment);
	}
}
