package com.github.colingan.infos.dal.constants;

import java.util.HashMap;
import java.util.Map;

public enum RoleGroup {

	ANOY(-1), // 匿名
	READ_ONLY(0), // 只读组
	READ_WRITE(1), // 发布组
	ADMIN(2) // 管理员
	;

	private static final Map<Integer, RoleGroup> VALUE_MAP = new HashMap<Integer, RoleGroup>() {
		{
			for (RoleGroup role : RoleGroup.values()) {
				put(role.getValue(), role);
			}
		}
	};

	private RoleGroup(int value) {
		this.value = value;
	}

	private int value;

	public int getValue() {
		return value;
	}

	public static boolean validateRoleGroupValue(int value) {
		return VALUE_MAP.containsKey(Integer.valueOf(value));
	}
}
