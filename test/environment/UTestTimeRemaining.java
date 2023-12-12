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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestTimeRemaining {

	@Test
	public void testDayTime() {
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment());
		assertEquals(12, timeRemaining.get());
		
		timeRemaining.use(6);
		assertEquals(6, timeRemaining.get());
		
		timeRemaining.use(6);
		assertEquals(0, timeRemaining.get());
	}
	
	@Test
	public void testDayTimeWithoutCalendar() {
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment());
		timeRemaining.reset(new MockCalendarSystem(false), false);
		assertEquals(11, timeRemaining.get());
	}
	
	@Test
	public void testDayTimeWithCalendar() {
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment());
		timeRemaining.reset(new MockCalendarSystem(true), false);
		assertEquals(11, timeRemaining.get());
		
		timeRemaining.reset(new MockCalendarSystem(false), true);
		assertEquals(11, timeRemaining.get());
		
		timeRemaining.reset(new MockCalendarSystem(true), true);
		assertEquals(12, timeRemaining.get());
	}
	
	@Test
	public void testNightTime() {
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment(true));
		assertEquals(18, timeRemaining.get());
		assertEquals("12 / 12, 6 / 6", timeRemaining.toString());
		
		timeRemaining.use(6);
		assertEquals(12, timeRemaining.get());
		assertEquals("6 / 12, 6 / 6", timeRemaining.toString());
		
		timeRemaining.use(6);
		assertEquals(6, timeRemaining.get());
		assertEquals("0 / 12, 6 / 6", timeRemaining.toString());
		
		timeRemaining.use(6);
		assertEquals(0, timeRemaining.get());
		assertEquals("0 / 12, 0 / 6", timeRemaining.toString());
	}
	
	@Test
	public void testCopy() {
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment(true));
		timeRemaining.use(6);
		
		assertEquals(12, timeRemaining.get());
		
		timeRemaining = timeRemaining.copy();
		assertEquals(18, timeRemaining.get());
	}
}
