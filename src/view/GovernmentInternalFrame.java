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
package view;


import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import organization.AvailableOrganizationPolicies;
import organization.DecisionCriteriaType;
import organization.Organization;
import organization.OrganizationPolicy;
import society.Society;

public class GovernmentInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
    private static final int xOffset = 0, yOffset = 300;

    private final OrganizationPanel contentPanel;
    private Society society;

	private JLabel lblMembershipValue;

    public GovernmentInternalFrame(Society society) {
        super("Government Info");
        this.society = society;
        
        setBounds(xOffset, yOffset, 700, 450);

        Organization organization = society.findOrganization();
        DecisionCriteriaType decisionCriteriaType = organization != null ? organization.getDecisionCriteriaType() : null;
		OrganizationPolicy organizationPolicy = organization != null ? organization.getOrganizationPolicy() : null;
        
        this.contentPanel = new OrganizationPanel(decisionCriteriaType, false, organizationPolicy, AvailableOrganizationPolicies.READONLY);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblMembership = new JLabel("Membership:");
		lblMembership.setBounds(10, 207, 74, 13);
		contentPanel.add(lblMembership);
		
		lblMembershipValue = new JLabel(formatMembershipValue(organization));
		lblMembershipValue.setBounds(10, 227, 103, 13);
		contentPanel.add(lblMembershipValue);
    }

	@Override
	public void updateInfo() {
		Organization organization = society.findOrganization();
		DecisionCriteriaType decisionCriteriaType = organization != null ? organization.getDecisionCriteriaType() : null;
		OrganizationPolicy organizationPolicy = organization != null ? organization.getOrganizationPolicy() : null;
		contentPanel.updateInfo(decisionCriteriaType, organizationPolicy);
		
		lblMembershipValue.setText(formatMembershipValue(organization));
	}
	
	private String formatMembershipValue(Organization organization) {
		int organizationSize = organization != null ? organization.size() : 0;
		return organizationSize + " / "  + society.size();
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}