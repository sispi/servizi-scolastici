package it.kdm.docer.core.authentication.helper;

import com.google.common.base.Joiner;

import java.util.Collection;

/**
 * Created by Łukasz Kwasek on 16/02/15.
 */
public class SSOLoginCredentialHelper {

    public static String makeGroups(Collection<String> groups) {
        return Joiner.on(";").skipNulls().join(groups);
    }

}
