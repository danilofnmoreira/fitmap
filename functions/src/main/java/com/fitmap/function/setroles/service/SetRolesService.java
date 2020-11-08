package com.fitmap.function.setroles.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.fitmap.function.common.config.CloudServicesConfig;
import com.fitmap.function.setroles.exception.RolesAlreadySettedException;
import com.fitmap.function.setroles.payload.request.SetRolesRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetRolesService {

    private static final String ROLES_KEY = "roles";
    private static final FirebaseAuth FIREBASE_AUTH = CloudServicesConfig.FIREBASE_AUTH;

    @SneakyThrows
    public static void setRoles(SetRolesRequest request) {

        var firebaseToken = verifyIdToken(request.getIdToken());

        checkRolesAlreadySetted(firebaseToken);

        var uid = firebaseToken.getUid();
        var userType = request.getUserType().name();

        setRoles(uid, userType);

    }

    private static void checkRolesAlreadySetted(FirebaseToken firebaseToken) {

        var claims = firebaseToken.getClaims();

        if (MapUtils.isEmpty(claims) || 
            Objects.isNull(claims.get(ROLES_KEY)) ||
            CollectionUtils.isEmpty((Collection) claims.get(ROLES_KEY))) {

            return;
        }

        throw new RolesAlreadySettedException(String.format("User, %s, already has roles.", firebaseToken.getEmail()));
    }

    private static void setRoles(String uid, String userType) throws FirebaseAuthException {

        var rolesValues = List.of("ROLE_" + userType, "ROLE_USER");

        var claims = new HashMap<String, Object>();
        claims.put(ROLES_KEY, rolesValues);

        FIREBASE_AUTH.setCustomUserClaims(uid, claims);
    }

    private static FirebaseToken verifyIdToken(String idToken) throws FirebaseAuthException {

        return FIREBASE_AUTH.verifyIdToken(idToken);
    }

}
