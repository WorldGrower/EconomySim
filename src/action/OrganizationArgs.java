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

import organization.DecisionCriteria;
import organization.OrganizationPolicy;

public class OrganizationArgs {
	public static final OrganizationArgs NONE = new OrganizationArgs(null, null);
	
	private DecisionCriteria decisionCriteria;
	private OrganizationPolicy organizationPolicy;
	
	public OrganizationArgs(DecisionCriteria decisionCriteria, OrganizationPolicy organizationPolicy) {
		super();
		this.decisionCriteria = decisionCriteria;
		this.organizationPolicy = organizationPolicy;
	}

	public DecisionCriteria getDecisionCriteria() {
		return decisionCriteria;
	}

	public OrganizationPolicy getOrganizationPolicy() {
		return organizationPolicy;
	}
}
