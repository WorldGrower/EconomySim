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
import java.awt.Dimension;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import asset.AssetType;
import society.PerformedTrade;
import society.Society;

public class TradeOverviewInternalFrame extends AbstractInternalNextTurnFrame implements NextTurnListener {
    private static final int xOffset = 0, yOffset = 300;
    private final TradeOverviewTableModel tradeOverviewTableModel;
    private Society society;
    
    public TradeOverviewInternalFrame(Society society) {
        super("Trade Overview");
        this.society = society;
        
        setBounds(xOffset, yOffset, 700, 450);
        this.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(null) {
        	@Override
        	public Dimension getPreferredSize() {
        		return new Dimension(this.getWidth(), this.getHeight());
        	}
        	
        };
        
        JScrollPane jScrollPane = createScrollPane(contentPanel);
        
        tradeOverviewTableModel = new TradeOverviewTableModel();
        JTable tradeTable = new DefaultJTable(tradeOverviewTableModel);
        jScrollPane.setViewportView(tradeTable);
    }
    
    private JScrollPane createScrollPane(JPanel contentPanel) {
		JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBounds(0, 0, this.getWidth() - 20, this.getHeight() - 50);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        
        return scrollPane;
	}

	@Override
	public void updateInfo() {
		tradeOverviewTableModel.updateTradeInfo(society);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
		      JFrame frame = new JFrame();
		      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		      JDesktopPane desktopPane = new JDesktopPane();
		      Society society = new Society();
		      society.addPerformedTrade(new PerformedTrade("personName", AssetType.CLAY_POT, 10, "personName2", AssetType.ANIMAL_HIDE_CLOTHES, 9));
		      TradeOverviewInternalFrame tradeOverviewInternalFrame = new TradeOverviewInternalFrame(society);
		      tradeOverviewInternalFrame.setLocation(0, 0);
		      tradeOverviewInternalFrame.setVisible(true);
		      desktopPane.add(tradeOverviewInternalFrame);

		      frame.add(desktopPane, BorderLayout.CENTER);
		      frame.setSize(700, 500);
		      frame.setVisible(true);
		    });
	}

	@Override
	public void updateSociety(Society society) {
		this.society = society;
		updateInfo();
	}
}