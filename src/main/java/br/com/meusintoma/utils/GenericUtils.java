package br.com.meusintoma.utils;

import java.util.UUID;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;

public class GenericUtils {

    public static void compareId(UUID targetId, UUID checkedId){
        boolean isEqual = targetId.equals(checkedId);
        if(!isEqual){
            throw new CustomAccessDeniedException("Os ids não correspondem");
        }
    }
}
