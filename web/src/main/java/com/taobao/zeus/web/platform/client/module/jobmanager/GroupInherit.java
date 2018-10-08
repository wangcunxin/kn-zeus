package com.taobao.zeus.web.platform.client.module.jobmanager;

import java.io.Serializable;

public class GroupInherit implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private GroupInherit parentGroup;
	private String groupId;

	public void setParentGroup(GroupInherit parentGroup) {
		this.parentGroup = parentGroup;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public GroupInherit getParentGroup() {
		return parentGroup;
	}

	public String getGroupId() {
		return groupId;
	}
}
