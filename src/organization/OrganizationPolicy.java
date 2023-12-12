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

import java.io.Serializable;

import asset.AssetType;
import environment.Payment;
import society.TheftPunishment;

public class OrganizationPolicy implements Serializable {
	public static final OrganizationPolicy DEFAULT = new OrganizationPolicy(
			TheftPunishment.DEATH_FOR_KNOWN_THIEF, 
			new Payment(AssetType.WHEAT, 0), 
			new Payment(AssetType.WHEAT, 100), 
			6);
	
	private final TheftPunishment theftPunishment;
	private final Payment taxes;
	private final Payment socialOrderMaintainerWage;
	private final int socialOrderMaintainerHoursWorked;

	public OrganizationPolicy(TheftPunishment theftPunishment, Payment taxes, Payment socialOrderMaintainerWage, int socialOrderMaintainerHoursWorked) {
		super();
		this.theftPunishment = theftPunishment;
		this.taxes = taxes;
		this.socialOrderMaintainerWage = socialOrderMaintainerWage;
		this.socialOrderMaintainerHoursWorked = socialOrderMaintainerHoursWorked;
	}
	
	public OrganizationPolicy(TheftPunishment theftPunishment, AssetType taxesPaymentType, int taxesQuantity, AssetType socialOrderMaintainerWageAssetType, int socialOrderMaintainerWageQuantity, int socialOrderMaintainerHoursWorked) {
		super();
		this.theftPunishment = theftPunishment;
		this.taxes = new Payment(taxesPaymentType, taxesQuantity);
		this.socialOrderMaintainerWage = new Payment(socialOrderMaintainerWageAssetType, socialOrderMaintainerWageQuantity);
		this.socialOrderMaintainerHoursWorked = socialOrderMaintainerHoursWorked;
	}

	public TheftPunishment getTheftPunishment() {
		return theftPunishment;
	}
	
	public Payment getTaxes() {
		return taxes;
	}

	public Payment getSocialOrderMaintainerWage() {
		return socialOrderMaintainerWage;
	}

	public int getSocialOrderMaintainerHoursWorked() {
		return socialOrderMaintainerHoursWorked;
	}

	@Override
	public String toString() {
		return "[TheftPunishment: " + theftPunishment.toString() + "]";
	}

	public OrganizationPolicy changeTaxes(Payment newTaxes) {
		return new OrganizationPolicy(theftPunishment, newTaxes, socialOrderMaintainerWage, socialOrderMaintainerHoursWorked);
	}
}
