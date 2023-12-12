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
package action;

import asset.Asset;
import asset.AssetType;
import person.Person;
import society.PerformedTrade;

public class TradeArgs {
	private final AssetType personAssetType;
	private final int personQuantity;
	private final AssetType targetPersonAssetType;
	private final int targetPersonQuantity;
	
	private TradeArgs(AssetType targetPersonAssetType, int targetPersonQuantity) {
		this(null, 0, targetPersonAssetType, targetPersonQuantity);
	}
	
	public TradeArgs(AssetType personAssetType, int personQuantity, AssetType targetPersonAssetType, int targetPersonQuantity) {
		super();
		this.personAssetType = personAssetType;
		this.personQuantity = personQuantity;
		this.targetPersonAssetType = targetPersonAssetType;
		this.targetPersonQuantity = targetPersonQuantity;
	}
	
	public PerformedTrade perform(Person person, Person targetPerson) {
		if (isNormalTrade()) {
			if (!targetAceptsTradePerform(person, targetPerson)) {
				return null; //TODO: should be replaced by PersonDecisions
			}
		}
		if (personAssetType != null) {
			Asset asset = person.getAssets().retrieve(personAssetType, personQuantity);
			targetPerson.getAssets().addAsset(asset);
		}
		Asset targetAsset = targetPerson.getAssets().retrieve(targetPersonAssetType, targetPersonQuantity);
		person.getAssets().addAsset(targetAsset);
		
		if (personAssetType != null) {
			return new PerformedTrade(person.toString(), personAssetType, personQuantity, targetPerson.toString(), targetPersonAssetType, targetPersonQuantity);
		}
		return null;
	}
	
	private boolean isNormalTrade() {
		return personAssetType != null;
	}

	public boolean targetAceptsTradePerform(Person person, Person targetPerson) {
		int timeForPersonAssets = TimeRequiredPerProducedAssetCalculator.calculate(personAssetType) * personQuantity;
		int timeForTargetPersonAssets = TimeRequiredPerProducedAssetCalculator.calculate(targetPersonAssetType) * targetPersonQuantity;
		return timeForPersonAssets >= timeForTargetPersonAssets;//TODO
	}
	
	@Override
	public String toString() {
		return "TradeArgs [personAssetType=" + personAssetType + ", personQuantity=" + personQuantity + ", targetPersonAssetType="
				+ targetPersonAssetType + ", targetPersonQuantity=" + targetPersonQuantity + "]";
	}

	public static int neededQuantityForAcceptableTrade(AssetType sourceAssetType, int sourceQuantity, AssetType returnAssetType) {
		int timeForSourceAssets = TimeRequiredPerProducedAssetCalculator.calculate(sourceAssetType) * sourceQuantity;
		return timeForSourceAssets / TimeRequiredPerProducedAssetCalculator.calculate(returnAssetType) + 1;
	}

	public static TradeArgs createTargetArgs(AssetType targetPersonAssetType, int targetPersonQuantity) {
		return new TradeArgs(targetPersonAssetType, targetPersonQuantity);
	}
	
	public static TradeArgs createTradeArgs(AssetType personAssetType, int personQuantity, AssetType targetPersonAssetType, int targetPersonQuantity) {
		return new TradeArgs(personAssetType, personQuantity, targetPersonAssetType, targetPersonQuantity);
	}
}
