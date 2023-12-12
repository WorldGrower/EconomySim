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

import action.TradeArgs;
import asset.AssetType;
import asset.SimpleAsset;

public class UTestTradeArgs {

	@Test
	public void testPerform() {
		TradeArgs tradeArgs = new TradeArgs(AssetType.BREAD, 50, AssetType.FLOUR, 40);
		Person person = new Person(18, Sex.MALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.BREAD, 100));
		Person targetPerson = new Person(18, Sex.FEMALE);
		targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.FLOUR, 100));
		
		tradeArgs.perform(person, targetPerson);
		
		assertEquals(50, person.getAssets().getQuantityFor(AssetType.BREAD));
		assertEquals(40, person.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(60, targetPerson.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(50, targetPerson.getAssets().getQuantityFor(AssetType.BREAD));
	}
	
	@Test
	public void testTargetAcceptsTradePerform() {
		Person person = new Person(18, Sex.MALE);
		Person targetPerson = new Person(18, Sex.FEMALE);
		assertEquals(false, new TradeArgs(AssetType.FOOD, 50, AssetType.FLOUR, 40).targetAceptsTradePerform(person, targetPerson));
		assertEquals(true, new TradeArgs(AssetType.FLOUR, 50, AssetType.FOOD, 40).targetAceptsTradePerform(person, targetPerson));
	}
	
	@Test
	public void testNeededQuantityForAcceptableTrade() {
		assertEquals(106, TradeArgs.neededQuantityForAcceptableTrade(AssetType.FLOUR, 100, AssetType.WHEAT));
		assertEquals(96, TradeArgs.neededQuantityForAcceptableTrade(AssetType.WHEAT, 100, AssetType.FLOUR));
		assertEquals(10001, TradeArgs.neededQuantityForAcceptableTrade(AssetType.GLAZED_POT, 100, AssetType.FOOD));
		assertEquals(111, TradeArgs.neededQuantityForAcceptableTrade(AssetType.BREAD, 100, AssetType.WHEAT));
	}
	
	@Test
	public void testCreateTargetArgs() {
		TradeArgs tradeArgs = TradeArgs.createTargetArgs(AssetType.FLOUR, 40);
		Person person = new Person(18, Sex.MALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 100));
		Person targetPerson = new Person(18, Sex.FEMALE);
		targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.FLOUR, 100));
		
		tradeArgs.perform(person, targetPerson);
		
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.FOOD));
		assertEquals(40, person.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(60, targetPerson.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(0, targetPerson.getAssets().getQuantityFor(AssetType.FOOD));
	}
}
