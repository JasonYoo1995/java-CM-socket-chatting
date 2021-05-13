package core;

import kr.ac.konkuk.ccslab.cm.entity.CMGroup;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Group {
  public String groupName;
  public String channelInfo;
  public List<CMUser> userList = new ArrayList<>();

  public Group(CMGroup cmGroup) {
    this.groupName = cmGroup.getGroupName();
    String channelInfo = cmGroup.getMulticastChannelInfo().toString();
    this.channelInfo = channelInfo.substring(0,channelInfo.length()-1); // substring은 '\n'를 제거하기 위함
    Iterator<CMUser> userIterator = cmGroup.getGroupUsers().getAllMembers().iterator();
    while(userIterator.hasNext())
    {
      CMUser user = userIterator.next();
      userList.add(user);
    }
  }

  public Group(String[] tmp) {
    this.groupName = tmp[0];
    this.channelInfo = tmp[1];
    for (int i=2; i<tmp.length; i++){
      CMUser user = new CMUser();
      user.setName(tmp[i]);
      userList.add(user);
    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("==========================\n");
    sb.append(" * Group Name : "+groupName+"\n");
    sb.append(" * Channel Information : "+channelInfo + "\n");
    sb.append(" * User List :\n");
    for(CMUser user : userList){
      sb.append("     "+user.getName()+"\n");
    }
    sb.append("==========================");
    return sb.toString();
  }
}
