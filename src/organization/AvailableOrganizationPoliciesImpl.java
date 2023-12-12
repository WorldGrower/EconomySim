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

import action.PersonAction;
import environment.Payment;
import knowledge.Knowledge;
import society.PublicKnowledge;
import society.TheftPunishment;

public class AvailableOrganizationPoliciesImpl implements AvailableOrganizationPolicies {
	
	private final boolean requireOrganizationPolicy;
	private final boolean changeTaxesKnowledgeKnown;
	
	public AvailableOrganizationPoliciesImpl(PersonAction personAction, PublicKnowledge publicKnowledge) {
		this.requireOrganizationPolicy = personAction.requiresOrganizationPolicy();
		this.changeTaxesKnowledgeKnown = publicKnowledge.canObserveKnowledge(Knowledge.ARITHMETIC);
	}
	
	@Override
	public boolean canChangeTheftPolicy() {
		return requireOrganizationPolicy;
	}
	
	@Override
	public boolean canChangeTaxesWages() {
		return requireOrganizationPolicy && changeTaxesKnowledgeKnown;
	}

	@Override
	public OrganizationPolicy changePolicy(OrganizationPolicy oldOrganizationPolicy, OrganizationPolicy newOrganizationPolicy) {
		TheftPunishment theftPunishment = oldOrganizationPolicy.getTheftPunishment();
		if (canChangeTheftPolicy()) {
			theftPunishment = newOrganizationPolicy.getTheftPunishment();
		}
		Payment taxes = oldOrganizationPolicy.getTaxes();
		Payment socialOrderMaintainerWage = oldOrganizationPolicy.getSocialOrderMaintainerWage();
		int socialOrderMaintainerHoursWorked = oldOrganizationPolicy.getSocialOrderMaintainerHoursWorked();
		if (canChangeTaxesWages()) {
			taxes = newOrganizationPolicy.getTaxes();
			socialOrderMaintainerWage = newOrganizationPolicy.getSocialOrderMaintainerWage();
			socialOrderMaintainerHoursWorked = newOrganizationPolicy.getSocialOrderMaintainerHoursWorked();
		}
		return new OrganizationPolicy(theftPunishment, taxes, socialOrderMaintainerWage, socialOrderMaintainerHoursWorked);
	}
}
