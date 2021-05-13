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
  public boolean isEmpty;

  public Group(CMGroup cmGroup) {
    this.groupName = cmGroup.getGroupName();
    this.channelInfo = cmGroup.getMulticastChannelInfo().toString();
    Iterator<CMUser> userIterator = cmGroup.getGroupUsers().getAllMembers().iterator();
    while(userIterator.hasNext())
    {
      CMUser user = userIterator.next();
      userList.add(user);
    }
    if(userList.size()==0) isEmpty = true;
    else isEmpty = false;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getChannelInfo() {
    return channelInfo;
  }

  public List<CMUser> getUserList() {
    return userList;
  }

  public boolean isEmpty() {
    return isEmpty;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("==========================\n");
    sb.append(" * Group Name : "+groupName+"\n");
    sb.append(" * Channel Information : "+channelInfo);
    sb.append(" * User List :\n");
    for(CMUser user : userList){
      sb.append("     "+user.getName()+"\n");
    }
    sb.append("==========================");
    return sb.toString();
  }
}
