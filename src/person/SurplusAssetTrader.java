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

import java.io.Serializable;

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import action.TradeArgs;
import asset.AssetType;
import environment.PublicLocations;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class SurplusAssetTrader implements Serializable {

	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	public SurplusAssetTrader(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
	}
	
	public void tradeSurplusAssets(Person person, AssetType surplusAssetType, AssetType neededAssetType) {
		PersonAction personAction = PersonActionFactory.TRADE_PERSON_ACTION;
		if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
			Person targetPerson = personFinder.findPerson(p -> p != person && p.getAssets().getQuantityFor(neededAssetType) > 1);
			if (targetPerson != null) {
				TradeArgs tradeArgs = createTradeArgs(person, surplusAssetType, targetPerson, neededAssetType, 1);
				if (tradeArgs != null) {
					PersonActionArgs args = new PersonActionArgs(targetPerson, null, tradeArgs, OrganizationArgs.NONE, PersonDecisions.AI);
					personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
				}
			}
		}	
	}

	private TradeArgs createTradeArgs(Person person, AssetType personAssetType, Person targetPerson, AssetType targetAssetType, int targetQuantity) {
		int assetQuantity = person.getAssets().getQuantityFor(personAssetType);
		int neededQuantity = TradeArgs.neededQuantityForAcceptableTrade(targetAssetType, targetQuantity, personAssetType);
		if (neededQuantity <= assetQuantity) {
			assetQuantity = neededQuantity;
		} else {
			return null;
		}
		TradeArgs tradeArgs = TradeArgs.createTradeArgs(personAssetType, assetQuantity, targetAssetType, targetQuantity);
		boolean targetAcceptsTrade = tradeArgs.targetAceptsTradePerform(person, targetPerson);
		if (targetAcceptsTrade) {
			return tradeArgs;
		}
		return null;
	}
}
