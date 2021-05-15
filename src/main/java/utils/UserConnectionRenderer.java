package utils;

import core.UserConnection;
import java.awt.Component;
import java.awt.Image;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class UserConnectionRenderer implements ListCellRenderer<UserConnection> {

  private final DefaultListCellRenderer defaultRenderer;
  private final ImageIcon connectedIcon, disconnectedIcon;
  private static final int WIDTH = 8;
  private static final int HEIGHT = 8;

  public UserConnectionRenderer() {
    defaultRenderer = new DefaultListCellRenderer();
    Image connectedImage = new ImageIcon("src/main/resources/green_circle.png").getImage();
    connectedImage = connectedImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    connectedIcon = new ImageIcon(connectedImage);
    Image disconnectedImage = new ImageIcon("src/main/resources/red_circle.png").getImage();
    disconnectedImage = disconnectedImage.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
    disconnectedIcon = new ImageIcon(disconnectedImage);
  }

  @Override
  public Component getListCellRendererComponent(JList<? extends UserConnection> list,
      UserConnection value, int index, boolean isSelected, boolean cellHasFocus) {
    JLabel renderer = (JLabel) defaultRenderer
        .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    renderer.setText(value.username);
    renderer.setIcon(value.isConnected ? connectedIcon : disconnectedIcon);

    return renderer;
  }
}
