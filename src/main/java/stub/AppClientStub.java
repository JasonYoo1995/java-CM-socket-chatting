package stub;

import core.Group;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import java.util.ArrayList;
import java.util.List;

public class AppClientStub extends CMClientStub {
  public List<Group> groupList = new ArrayList<Group>();

  public AppClientStub() {
    super();
  }
}
