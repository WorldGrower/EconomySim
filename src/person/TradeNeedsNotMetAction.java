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
import java.util.List;

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import action.TradeArgs;
import asset.AssetType;
import environment.PublicLocations;
import person.FutureNeeds.NeedsNotMetAction;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class TradeNeedsNotMetAction implements NeedsNotMetAction, Serializable {

	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	public TradeNeedsNotMetAction(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
	}
	
	@Override
	public boolean perform(Person person, AssetType targetAssetType) {
		PersonAction personAction = PersonActionFactory.TRADE_PERSON_ACTION;
		if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
			Person targetPerson = personFinder.findPerson(p -> p != person && p.getAssets().getQuantityFor(targetAssetType) >= 100);
			if (targetPerson != null) {
				TradeArgs tradeArgs = createTradeArgs(person, targetPerson, targetAssetType, 100);
				if (tradeArgs != null) {
					PersonActionArgs args = new PersonActionArgs(targetPerson, null, tradeArgs, OrganizationArgs.NONE, PersonDecisions.AI);
					personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
				}
				return true;
			}
		}
		return false;
	}
	
	private TradeArgs createTradeArgs(Person person, Person targetPerson, AssetType targetAssetType, int targetQuantity) {
		List<AssetType> assetTypes = person.getAssets().getSortedAssetKeys();
		for(AssetType assetType : assetTypes) {
			if (assetType.isTradeable()) {
				int assetQuantity = person.getAssets().getQuantityFor(assetType);
				int neededQuantity = TradeArgs.neededQuantityForAcceptableTrade(targetAssetType, targetQuantity, assetType);
				if (neededQuantity <= assetQuantity) {
					assetQuantity = neededQuantity;
				}
				TradeArgs tradeArgs = TradeArgs.createTradeArgs(assetType, assetQuantity, targetAssetType, targetQuantity);
				boolean targetAcceptsTrade = tradeArgs.targetAceptsTradePerform(person, targetPerson);
				if (targetAcceptsTrade) {
					return tradeArgs;
				}
			}
		}
		return null;
	}
}
