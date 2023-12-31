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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UTestSocialOrderImpl {

	@Test
	public void testCalculateBaseThiefPenalty() {
		SocialOrderImpl socialOrderImpl = new SocialOrderImpl();
		assertEquals("5", socialOrderImpl.getBaseThiefPenaltyDescription());
		
		socialOrderImpl.startTurn();
		socialOrderImpl.endTurn(100);
		assertEquals("3", socialOrderImpl.getBaseThiefPenaltyDescription());
		
		socialOrderImpl.startTurn();
		for(int i=0; i<10; i++) { socialOrderImpl.maintainSocialOrder(); }
		socialOrderImpl.endTurn(100);
		assertEquals("5", socialOrderImpl.getBaseThiefPenaltyDescription());
	}
}
