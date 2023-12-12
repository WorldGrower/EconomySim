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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import main.SocietyFactory;
import society.Society;

public class MainDesktopFrame extends JFrame {
	private JDesktopPane desktop;

	private final SocietyInternalFrame societyInternalFrame;
	private final PersonDetailsInternalFrame personDetailsInternalFrame;
	private final PopulationGraphInternalFrame populationGraphInternalFrame;
	private final GovernmentInternalFrame governmentInternalFrame;
	private final PublicAssetsInternalFrame publicAssetsInternalFrame;
	private final TechnologyTreeInternalFrame technologyTreeInternalFrame;
	private final TradeOverviewInternalFrame tradeOverviewInternalFrame;
	private final ProfessionInternalFrame professionInternalFrame;
	private final NexTurnInternalFrame nextTurnInternalFrame;
	
	private final List<SocietyContainer> societyContainers = new ArrayList<>();
	
	private MainDesktopFrame(Society society) {
		super("Economy Sim");

		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

		desktop = new JDesktopPane();
		societyInternalFrame = new SocietyInternalFrame(society);
		personDetailsInternalFrame = new PersonDetailsInternalFrame(society, societyInternalFrame);
		societyInternalFrame.addPersonListViewListener(personDetailsInternalFrame);
		populationGraphInternalFrame = new PopulationGraphInternalFrame(society);
		governmentInternalFrame = new GovernmentInternalFrame(society);
		publicAssetsInternalFrame = new PublicAssetsInternalFrame(society);
		technologyTreeInternalFrame = new TechnologyTreeInternalFrame(society);
		tradeOverviewInternalFrame = new TradeOverviewInternalFrame(society);
		professionInternalFrame = new ProfessionInternalFrame(society);
		nextTurnInternalFrame = new NexTurnInternalFrame(society, personDetailsInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(societyInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(personDetailsInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(populationGraphInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(governmentInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(publicAssetsInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(technologyTreeInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(tradeOverviewInternalFrame);
		nextTurnInternalFrame.addNextTurnListener(professionInternalFrame);
		addInternalFrame(societyInternalFrame);
		addInternalFrame(personDetailsInternalFrame);
		addInternalFrame(populationGraphInternalFrame);
		addInternalFrame(governmentInternalFrame);
		addInternalFrame(nextTurnInternalFrame);
		addInternalFrame(publicAssetsInternalFrame);
		addInternalFrame(technologyTreeInternalFrame);
		addInternalFrame(tradeOverviewInternalFrame);
		addInternalFrame(professionInternalFrame);
		
		setContentPane(desktop);
		setJMenuBar(createMenuBar());
		
		showInternalFrame(societyInternalFrame);
		showInternalFrame(personDetailsInternalFrame);
		showInternalFrame(publicAssetsInternalFrame);
		showInternalFrame(nextTurnInternalFrame);
	}
	
	private void addInternalFrame(AbstractInternalFrame internalFrame) {
		desktop.add(internalFrame);
		societyContainers.add(internalFrame);
	}

	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		createFileMenu(menuBar);
		createWindowsMenu(menuBar);

		return menuBar;
	}
	
	private void createFileMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		createMenuItem(menu, "New", KeyEvent.VK_E, e -> newGame());
		createMenuItem(menu, "Load", KeyEvent.VK_L, e -> loadGame());
		createMenuItem(menu, "Save", KeyEvent.VK_S, e -> saveGame());
		createMenuItem(menu, "Quit", KeyEvent.VK_Q, e -> quit());
	}
	
	private void createWindowsMenu(JMenuBar menuBar) {
		JMenu menu = new JMenu("Windows");
		menu.setMnemonic(KeyEvent.VK_W);
		menuBar.add(menu);

		createMenuItem(menu, "Society", KeyEvent.VK_C, e -> showInternalFrame(societyInternalFrame));
		createMenuItem(menu, "Person", KeyEvent.VK_P, e -> showInternalFrame(personDetailsInternalFrame));
		createMenuItem(menu, "Population Graph", KeyEvent.VK_O, e -> showInternalFrame(populationGraphInternalFrame));
		createMenuItem(menu, "Government Info", KeyEvent.VK_G, e -> showInternalFrame(governmentInternalFrame));
		createMenuItem(menu, "Public Assets", KeyEvent.VK_A, e -> showInternalFrame(publicAssetsInternalFrame));
		createMenuItem(menu, "Technology", KeyEvent.VK_T, e -> showInternalFrame(technologyTreeInternalFrame));
		createMenuItem(menu, "Trade Overview", KeyEvent.VK_R, e -> showInternalFrame(tradeOverviewInternalFrame));
		createMenuItem(menu, "Profession Overview", KeyEvent.VK_F, e -> showInternalFrame(professionInternalFrame));
		
		createMenuItem(menu, "Next Turn", KeyEvent.VK_N, e -> showInternalFrame(nextTurnInternalFrame));
	}

	private void createMenuItem(JMenu menu, String text, int keyEvent, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setMnemonic(keyEvent);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyEvent, ActionEvent.ALT_MASK));
		menuItem.addActionListener(actionListener);
		menu.add(menuItem);
	}

	private static void showInternalFrame(JInternalFrame internalFrame) {
		internalFrame.setVisible(true);
		try {
			internalFrame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}
	
	private void newGame() {
		Society society = SocietyFactory.createDefaultInstance();
		updateSociety(society);
	}

	private void updateSociety(Society society) {
		for(SocietyContainer societyContainer : societyContainers) {
			societyContainer.updateSociety(society);
		}
	}
	
	private void loadGame() {
		GameSaveView gameSaveView = new GameSaveView();
		Society society = gameSaveView.loadGame(this);
		if (society != null) {
			updateSociety(society);
		}
	}
	
	private void saveGame() {
		GameSaveView gameSaveView = new GameSaveView();
		Society society = societyInternalFrame.getSociety();
		gameSaveView.saveGame(this, society);
	}

	protected void quit() {
		System.exit(0);
	}

	public static void createAndShowGUI(Society society) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		SwingUtilities.invokeLater(() -> {
			MainDesktopFrame frame = new MainDesktopFrame(society);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}
