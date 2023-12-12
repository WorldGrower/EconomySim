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

public class UTestSocialObligationFactory {

	@Test
	public void testCreatePayment() {
		Assets assets = new Assets();
		SocialObligation socialObligation = SocialObligationFactory.create(SocialObligationType.TAXES, AssetType.WHEAT, 1, assets);
		Person person = new Person(18, Sex.FEMALE);
		
		assertEquals(false, socialObligation.canFullfill(person));
		assertEquals(false, socialObligation.getType() == SocialObligationType.EMPLOYEE);
		assertEquals(false, socialObligation.isDone());
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.WHEAT, 100));
		assertEquals(true, socialObligation.canFullfill(person));
		
		socialObligation.fullfill(person);
		assertEquals(99, person.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(1, assets.getQuantityFor(AssetType.WHEAT));
		assertEquals(true, socialObligation.isDone());
	}
	
	@Test
	public void testCreateCommunityService() {
		PersonAction personAction = PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION;
		SocialObligation socialObligation = SocialObligationFactory.create(SocialObligationType.FORCED_LABOR, personAction, 2, 10, new MockPublicAssets(), new MockPublicOrganizations());
		Person person = new Person(18, Sex.FEMALE);
		
		assertEquals(true, socialObligation.canFullfill(person));
		assertEquals(true, socialObligation.getType() == SocialObligationType.FORCED_LABOR);
		assertEquals(false, socialObligation.getType() == SocialObligationType.TAXES);
		assertEquals(false, socialObligation.isDone());
		
		for(int i=0; i<10; i++) { socialObligation.endTurn(); }
		assertEquals(true, socialObligation.isDone());
		
		assertEquals(12, person.getTimeRemaining().get());
		socialObligation.fullfill(person);
		assertEquals(10, person.getTimeRemaining().get());
		assertEquals(true, socialObligation.isDone());
	}	
	
	@Test
	public void testCreateEmployment() {
		PersonAction personAction = PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION;
		SocialObligation socialObligation = SocialObligationFactory.create(SocialObligationType.EMPLOYEE, personAction, 2, new MockPublicAssets(), new MockPublicOrganizations());
		Person person = new Person(18, Sex.FEMALE);
		
		assertEquals(true, socialObligation.canFullfill(person));
		assertEquals(true, socialObligation.getType() == SocialObligationType.EMPLOYEE);
		assertEquals(false, socialObligation.isDone());
		
		socialObligation.fullfill(person);
		assertEquals(true, socialObligation.isDone());
	}
}