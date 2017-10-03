package org.jaweze.hello.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum CustomRoles {

    ADMIN,
    MANAGER(ADMIN),
    VIEWER(MANAGER, ADMIN);

    private final List<CustomRoles> parentRoles;

    CustomRoles(CustomRoles... parentRoles) {
        this.parentRoles = Arrays.asList(parentRoles);
    }

    public Set<CustomRoles> getMatchingRoles() {
        Set<CustomRoles> result = new HashSet<>();
        result.add(this);
        for (CustomRoles parentRole : parentRoles) {
            result.addAll(parentRole.getMatchingRoles());
        }
        return result;
    }
}
