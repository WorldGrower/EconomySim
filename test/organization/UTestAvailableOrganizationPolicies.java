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
package organization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import action.PersonAction;
import action.PersonActionFactory;
import knowledge.Knowledge;
import society.MockPublicKnowledge;
import society.PublicKnowledge;

public class UTestAvailableOrganizationPolicies {

	@Test
	public void testChangeChangePoliciesReadOnly() {
		assertEquals(false, AvailableOrganizationPolicies.READONLY.canChangeTheftPolicy());
		assertEquals(false, AvailableOrganizationPolicies.READONLY.canChangeTaxesWages());
	}
	
	@Test
	public void testChangeChangePolicies() {
		PersonAction personAction = PersonActionFactory.CHANGE_POLICY_PERSON_ACTION;
		PublicKnowledge publicKnowledge = new MockPublicKnowledge(Knowledge.ARITHMETIC);
		AvailableOrganizationPolicies availableOrganizationPolicies = new AvailableOrganizationPoliciesImpl(personAction, publicKnowledge);
		
		assertEquals(true, availableOrganizationPolicies.canChangeTheftPolicy());
		assertEquals(true, availableOrganizationPolicies.canChangeTaxesWages());
	}
}
