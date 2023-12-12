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
package asset;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestOperationalAssetAttribute {

	@Test
	public void testIsOperationalFinished() {
		OperationalAssetAttribute attribute = new OperationalAssetAttribute();
		
		assertEquals(false, attribute.isOperational());
		assertEquals(false, attribute.isFinished());
		
		for(int i=0; i<1000; i++) { attribute.increaseOperational(); }

		assertEquals(true, attribute.isOperational());
		assertEquals(true, attribute.isFinished());
		
		for(int i=0; i<10; i++) { attribute.decreaseOperational(); }
		
		assertEquals(false, attribute.isOperational());
		assertEquals(true, attribute.isFinished());
	}
	
	@Test
	public void testEndOfTurn() {
		OperationalAssetAttribute attribute = new OperationalAssetAttribute();
		
		for(int i=0; i<100; i++) {
			assertEquals(false, attribute.endOfTurn());
		}
		
		for(int i=0; i<1000; i++) { attribute.increaseOperational(); }

		for(int i=0; i<99; i++) {
			assertEquals(false, attribute.endOfTurn());
		}
		
		assertEquals(true, attribute.endOfTurn());
	}
}
