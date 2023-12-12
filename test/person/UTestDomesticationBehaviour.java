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
package person;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.LocatableAsset;
import environment.Location;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestDomesticationBehaviour {

	@Test
	public void testCreateDog() {
		Person person = createPerson();
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		MockPublicKnowledge publicKnowledge = new MockPublicKnowledge();
		DomesticationBehaviour domesticationBehaviour = new DomesticationBehaviour(new MockPersonFinder(), publicOrganizations, new MockPublicAssets(), publicKnowledge, new PublicLocationsImpl());
		
		assertEquals(false, person.hasKnowledge(Knowledge.DOMESTICATION_OF_DOG));
		
		while (person.getAssets().getQuantityFor(AssetType.DOG) == 0) {
			domesticateDog(person, publicOrganizations, domesticationBehaviour);
		}
		
		assertEquals(true, person.hasKnowledge(Knowledge.DOMESTICATION_OF_DOG));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.DOG));
		
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.DOMESTICATION_OF_DOG));
		
		domesticateDog(person, publicOrganizations, domesticationBehaviour);
		
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.DOG));
	}

	private Person createPerson() {
		Person person = new Person(18, Sex.FEMALE);
		person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		return person;
	}
	
	@Test
	public void testCreateCattle() {
		Person person = createPerson();
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		MockPublicKnowledge publicKnowledge = new MockPublicKnowledge();
		DomesticationBehaviour domesticationBehaviour = new DomesticationBehaviour(new MockPersonFinder(), publicOrganizations, new MockPublicAssets(), publicKnowledge, new PublicLocationsImpl());
		
		assertEquals(false, person.hasKnowledge(Knowledge.DOMESTICATION_OF_CATTLE));
		
		while (person.getAssets().getQuantityFor(AssetType.CATTLE) == 0) {
			domesticateCattle(person, publicOrganizations, domesticationBehaviour);
		}
		
		assertEquals(true, person.hasKnowledge(Knowledge.DOMESTICATION_OF_CATTLE));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.CATTLE));
		
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.DOMESTICATION_OF_CATTLE));
		
		domesticateCattle(person, publicOrganizations, domesticationBehaviour);
		
		assertEquals(2, person.getAssets().getQuantityFor(AssetType.CATTLE));
	}

	private void domesticateDog(Person person, MockPublicOrganizations publicOrganizations, DomesticationBehaviour domesticationBehaviour) {
		domesticationBehaviour.processActionsForDog(person, PersonDecisions.AI);
		person.getTimeRemaining().reset(publicOrganizations, true);
	}
	
	private void domesticateCattle(Person person, MockPublicOrganizations publicOrganizations, DomesticationBehaviour domesticationBehaviour) {
		domesticationBehaviour.processActionsForCattle(person, PersonDecisions.AI);
		person.getTimeRemaining().reset(publicOrganizations, true);
		person.getAssets().regenerate();
	}
}
