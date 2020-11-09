package com.fitmap.function.setroles.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.fitmap.function.common.exception.TokenExpiredException;
import com.fitmap.function.setroles.exception.RolesAlreadySettedException;
import com.fitmap.function.setroles.payload.request.SetRolesRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class SetRolesService {

    private static final String ROLES_KEY = "roles";

    private final FirebaseAuth firebaseAuth;

    public void setRoles(SetRolesRequest request) {

        var firebaseToken = verifyIdToken(request.getIdToken());

        checkRolesAlreadySetted(firebaseToken);

        var uid = firebaseToken.getUid();
        var userType = request.getUserType().name();

        setRoles(uid, userType);

    }

    private void checkRolesAlreadySetted(FirebaseToken firebaseToken) {

        var claims = firebaseToken.getClaims();

        if (MapUtils.isEmpty(claims) || 
            Objects.isNull(claims.get(ROLES_KEY)) ||
            CollectionUtils.isEmpty((Collection) claims.get(ROLES_KEY))) {

            return;
        }

        throw new RolesAlreadySettedException();
    }

    @SneakyThrows
    private void setRoles(String uid, String userType) {

        var rolesValues = List.of("ROLE_" + userType, "ROLE_USER");

        var claims = new HashMap<String, Object>();
        claims.put(ROLES_KEY, rolesValues);

        firebaseAuth.setCustomUserClaims(uid, claims);
    }

    private FirebaseToken verifyIdToken(String idToken) {

        try {

            return firebaseAuth.verifyIdToken(idToken);
        } catch (Exception e) {

            throw new TokenExpiredException();
        }
    }

}
