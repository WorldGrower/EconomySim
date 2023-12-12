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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import asset.AssetType;
import asset.LocatableAsset;
import environment.Location;
import organization.DecisionCriteriaFactory;
import organization.Organization;
import society.MockPublicAssets;
import society.MockPublicOrganizations;
import society.PublicAssets;
import society.PublicOrganizations;

public class UTestPregnancy {

	@Test
	public void testJoinsParentOrganization() {
		Person parent = new Person(18, Sex.FEMALE);
		Pregnancy pregnancy = new Pregnancy(parent, new Random(0), PersonBehaviour.NONE);
		pregnancy.impregnate();
		
		List<Person> personList = new ArrayList<>();
		Organization organization = new Organization(parent, DecisionCriteriaFactory.createRepublic());
		PublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
		for (int i=0; i<8; i++) { pregnancy.processTurn(personList, publicOrganizations, new MockPublicAssets()); }
		
		assertEquals(1, personList.size());
		assertEquals(true, organization.contains(personList.get(0)));
	}
	
	@Test
	public void testReceivesPublicLand() {
		Person parent = new Person(18, Sex.FEMALE);
		
		Pregnancy pregnancy = new Pregnancy(parent, new Random(0), PersonBehaviour.NONE);
		pregnancy.impregnate();
		
		PublicAssets publicAssets = new MockPublicAssets(new LocatableAsset(AssetType.LAND, new Location(0), new Location(1)));
		List<Person> personList = new ArrayList<>();
		for (int i=0; i<8; i++) { pregnancy.processTurn(personList, new MockPublicOrganizations(), publicAssets); }
		
		assertEquals(1, personList.size());
		assertEquals(1, personList.get(0).getAssets().getQuantityFor(AssetType.LAND));
		assertEquals(1, publicAssets.get(AssetType.LAND).getQuantity());
	}
	
	@Test
	public void testReceivesPublicLandAssets() {
		Person parent = new Person(18, Sex.FEMALE);
		
		Pregnancy pregnancy = new Pregnancy(parent, new Random(0), PersonBehaviour.NONE);
		pregnancy.impregnate();
		
		MockPublicAssets publicAssets = new MockPublicAssets(new LocatableAsset(AssetType.LAND, new Location(0), new Location(1)));
		publicAssets.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, new Location(0)));
		List<Person> personList = new ArrayList<>();
		for (int i=0; i<8; i++) { pregnancy.processTurn(personList, new MockPublicOrganizations(), publicAssets); }
		
		assertEquals(1, personList.size());
		assertEquals(1, personList.get(0).getAssets().getQuantityFor(AssetType.LAND));
		assertEquals(1, personList.get(0).getAssets().getQuantityFor(AssetType.WHEAT_FIELD));
		assertEquals(1, publicAssets.get(AssetType.LAND).getQuantity());
		
	}
}
