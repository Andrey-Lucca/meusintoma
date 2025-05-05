package br.com.meusintoma.utils;

import java.util.List;
import java.util.UUID;

import br.com.meusintoma.exceptions.globalCustomException.CustomAccessDeniedException;
import br.com.meusintoma.exceptions.globalCustomException.NoContentException;

public class GenericUtils {

    public static void compareId(UUID targetId, UUID checkedId){
        boolean isEqual = targetId.equals(checkedId);
        if(!isEqual){
            throw new CustomAccessDeniedException("Os ids não correspondem");
        }
    }

    public static <T> void checkIsEmptyList(List<T> genericList){
        boolean isEmpty = genericList.isEmpty();
        if(isEmpty){
            throw new NoContentException("Nenhum resultado disponível a partir dessa busca");
        }
    }
}
