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
import asset.Assets;
import asset.LocatableAsset;
import asset.MockAssetContainer;
import asset.SimpleAsset;
import environment.Location;
import organization.DecisionCriteriaFactory;
import organization.Organization;

public class UTestFamily {

	@Test
	public void testAddChild() {
		Person parent = new Person(18, Sex.FEMALE);
		Person child = new Person(18, Sex.FEMALE);
		
		parent.getFamily().addChild(parent, child);
		
		assertEquals(1, parent.getFamily().getChildren().size());
		assertEquals(child, parent.getFamily().getChildren().get(0));
		assertEquals(1, child.getFamily().getParents().size());
		assertEquals(parent, child.getFamily().getParents().get(0));
	}
	
	@Test
	public void testOnDeath() {
		Person parent = new Person(18, Sex.FEMALE);
		Person person = new Person(18, Sex.FEMALE);
		Person partner = new Person(18, Sex.FEMALE);
		Person child = new Person(18, Sex.FEMALE);
		
		parent.getFamily().addChild(parent, person);
		person.getFamily().setPartner(person, partner);
		person.getFamily().addChild(person, child);
		
		person.getFamily().onDeath(person);
		
		assertEquals(0, parent.getFamily().getChildren().size());
		assertEquals(1, child.getFamily().getParents().size());
		assertEquals(partner, child.getFamily().getParents().get(0));
		assertEquals(null, partner.getFamily().getPartner());
	}
	
	@Test
	public void testFindAssetToWorkOn() {
		Family family = new Family();
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		assertEquals(AssetType.LAND, family.findAssetToWorkOn(assets, AssetType.FOOD).getAssetType());
	}
	
	@Test
	public void testFindAssetToWorkOnPartner() {
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		partner.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 5));
		person.getFamily().setPartner(person, partner);
		
		assertEquals(AssetType.LAND, person.getFamily().findAssetToWorkOn(person.getAssets(), AssetType.FOOD).getAssetType());
	}
	
	@Test
	public void testFindAssetToWorkOnPartnerOtherAssets() {
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		partner.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 5));
		person.getFamily().setPartner(partner, person);
		person.getAssets().addAsset(new SimpleAsset(AssetType.CASH, 5));
		
		assertEquals(AssetType.LAND, person.getFamily().findAssetToWorkOn(person.getAssets(), AssetType.FOOD).getAssetType());
	}
	
	@Test
	public void testFindAssetToWorkOnParents() {
		Person person = new Person(18, Sex.MALE);
		Person parent = new Person(18, Sex.FEMALE);
		parent.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 5));
		parent.getFamily().addChild(parent, person);
		
		assertEquals(AssetType.LAND, person.getFamily().findAssetToWorkOn(person.getAssets(), AssetType.FOOD).getAssetType());
	}
	
	@Test
	public void testGetAsset() {
		Family family = new Family();
		Assets assets = new Assets();
		assets.addAsset(new SimpleAsset(AssetType.LAND, 5));
		
		assertEquals(AssetType.LAND, family.getAsset(assets, AssetType.LAND).getAssetType());
	}
	
	@Test
	public void testGetAssetPartner() {
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		partner.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 5));
		person.getFamily().setPartner(person, partner);
		
		assertEquals(AssetType.LAND, person.getFamily().getAsset(person.getAssets(), AssetType.LAND).getAssetType());
	}
	
	@Test
	public void testGetAssetParents() {
		Person person = new Person(18, Sex.MALE);
		Person parent = new Person(18, Sex.FEMALE);
		parent.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 5));
		parent.getFamily().addChild(parent, person);
		
		assertEquals(AssetType.LAND, person.getFamily().getAsset(person.getAssets(), AssetType.LAND).getAssetType());
	}
	
	@Test
	public void testDivideAssetsWithPartner() {
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		MockAssetContainer assetContainer = new MockAssetContainer();
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 6));
		person.getFamily().setPartner(person, partner);
		
		person.getFamily().divideAssets(person.getAssets(), assetContainer);
		assertEquals(6, partner.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(0, assetContainer.getLand());
	}
	
	@Test
	public void testDivideAssetsWithChildren() {
		Person person = new Person(18, Sex.MALE);
		Person child1 = new Person(18, Sex.FEMALE);
		Person child2 = new Person(18, Sex.FEMALE);
		MockAssetContainer assetContainer = new MockAssetContainer();
		
		Location[] locations = {
				new Location(0),
				new Location(1),
				new Location(2),
				new Location(3),
				new Location(4),
				new Location(5),
		};
		
		person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, locations));
		person.getFamily().addChild(person, child1);
		person.getFamily().addChild(person, child2);
		
		person.getFamily().divideAssets(person.getAssets(), assetContainer);
		assertEquals(3, child1.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(3, child2.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(0, assetContainer.getLand());
	}
	
	@Test
	public void testDivideAssetsWithPartnerAndChildren() {
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		Person child1 = new Person(18, Sex.FEMALE);
		Person child2 = new Person(18, Sex.FEMALE);
		Person child3 = new Person(18, Sex.FEMALE);
		MockAssetContainer assetContainer = new MockAssetContainer();
		
		Location[] locations = {
				new Location(0),
				new Location(1),
				new Location(2),
				new Location(3),
				new Location(4),
				new Location(5),
		};
		
		person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, locations));
		person.getAssets().addAsset(new SimpleAsset(AssetType.CASH, 100));
		person.getFamily().setPartner(person, partner);
		person.getFamily().addChild(person, child1);
		person.getFamily().addChild(person, child2);
		person.getFamily().addChild(person, child3);
		
		person.getFamily().divideAssets(person.getAssets(), assetContainer);
		
		assertEquals(1, partner.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(1, child1.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(1, child2.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(3, child3.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(0, assetContainer.getLand());
		
		assertEquals(25, partner.getAssets().get(AssetType.CASH).getQuantity());
		assertEquals(25, child1.getAssets().get(AssetType.CASH).getQuantity());
		assertEquals(25, child2.getAssets().get(AssetType.CASH).getQuantity());
		assertEquals(25, child3.getAssets().get(AssetType.CASH).getQuantity());
		assertEquals(0, assetContainer.getCash());
	}
	
	@Test
	public void testDivideAssetsLimitedLand() {
		Person person = new Person(18, Sex.MALE);
		Person child1 = new Person(18, Sex.FEMALE);
		Person child2 = new Person(18, Sex.FEMALE);
		Person child3 = new Person(18, Sex.FEMALE);
		MockAssetContainer assetContainer = new MockAssetContainer();
		
		Location[] locations = {
				new Location(0),
		};
		
		person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, locations));
		person.getFamily().addChild(person, child1);
		person.getFamily().addChild(person, child2);
		person.getFamily().addChild(person, child3);
		
		person.getFamily().divideAssets(person.getAssets(), assetContainer);
		
		assertEquals(1, child1.getAssets().get(AssetType.LAND).getQuantity());
		assertEquals(null, child2.getAssets().get(AssetType.LAND));
		assertEquals(null, child3.getAssets().get(AssetType.LAND));
		assertEquals(0, assetContainer.getLand());
	}
	
	@Test
	public void testParentIsMemberOf() {
		Person person = new Person(18, Sex.MALE);
		Person parent = new Person(18, Sex.FEMALE);
		parent.getFamily().addChild(parent, person);
		
		Organization organization = new Organization(new Person(18, Sex.FEMALE), DecisionCriteriaFactory.createOligarchy());
		assertEquals(false, person.getFamily().parentIsMemberOf(parent, organization));
		
		organization.add(parent);
		assertEquals(true, person.getFamily().parentIsMemberOf(parent, organization));
	}
	
	@Test
	public void testGetQuantityFor() {
		Person person = new Person(18, Sex.MALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.KILN, 5));
		Person partner = new Person(18, Sex.FEMALE);
		partner.getAssets().addAsset(new SimpleAsset(AssetType.KILN, 1));
		person.getFamily().setPartner(person, partner);
		
		assertEquals(5, person.getAssets().getQuantityFor(AssetType.KILN));
		assertEquals(1, partner.getAssets().getQuantityFor(AssetType.KILN));
		
		assertEquals(6, person.getFamily().getQuantityFor(person.getAssets(), AssetType.KILN));
		assertEquals(6, partner.getFamily().getQuantityFor(partner.getAssets(), AssetType.KILN));
	}
}
