package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class ProfilePanel extends JPanel {

  private JScrollPane scrollPane;

  /**
   * Owner profile.
   */
  private JTextArea ownerInfoTextArea;

  /**
   * Other profiles panel.
   */
  private JPanel othersPanel;

  /**
   * Other profiles.
   */
  private JList<String> others;

  public ProfilePanel() {
    setLayout(new BorderLayout());

    ownerInfoTextArea = new JTextArea();
    ownerInfoTextArea.setFont(new Font("Nanum Gothic", Font.BOLD, 18));
    ownerInfoTextArea.setText("신윤섭");

    othersPanel = new JPanel();
    String[] sampleUsers = {"유경원", "유진욱", "한지희", "임민규"};
    others = new JList<>(sampleUsers);
    others.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    othersPanel.add(others);

    add(ownerInfoTextArea, BorderLayout.NORTH);
    add(othersPanel, BorderLayout.CENTER);
  }
}
