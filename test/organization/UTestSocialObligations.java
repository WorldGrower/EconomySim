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
import asset.AssetType;
import asset.Assets;
import asset.SimpleAsset;
import person.Person;
import person.Sex;
import society.MockPublicAssets;
import society.MockPublicOrganizations;

public class UTestSocialObligations {

	@Test
	public void testAddFullfill() {
		SocialObligations socialObligations = new SocialObligations();
		socialObligations.add(SocialObligationFactory.create(SocialObligationType.TAXES, AssetType.WHEAT, 1, new Assets()));
		
		assertEquals(1, socialObligations.size());
		
		Person person = new Person(18, Sex.FEMALE);
		assertEquals(false, socialObligations.canFullfill(0, person));
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.WHEAT, 100));
		assertEquals(true, socialObligations.canFullfill(0, person));
		
		socialObligations.fullfill(0, person);
		
		assertEquals(0, socialObligations.size());
		assertEquals(99, person.getAssets().getQuantityFor(AssetType.WHEAT));
	}
	
	@Test
	public void testEndTurn() {
		PersonAction personAction = PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION;
		SocialObligations socialObligations = new SocialObligations();
		socialObligations.add(SocialObligationFactory.create(SocialObligationType.EMPLOYEE, personAction, 2, 10, new MockPublicAssets(), new MockPublicOrganizations()));
		
		assertEquals(1, socialObligations.size());
		
		for(int i=0; i<10; i++) { socialObligations.endTurn(); }
		assertEquals(0, socialObligations.size());
	}
	
	@Test
	public void testHasSocialObligation() {
		PersonAction personAction = PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION;
		SocialObligations socialObligations = new SocialObligations();
		socialObligations.add(SocialObligationFactory.create(SocialObligationType.EMPLOYEE, personAction, 2, 10, new MockPublicAssets(), new MockPublicOrganizations()));

		assertEquals(true, socialObligations.hasSocialObligation(SocialObligationType.EMPLOYEE));
		assertEquals(false, socialObligations.hasSocialObligation(SocialObligationType.TAXES));
	}
}
