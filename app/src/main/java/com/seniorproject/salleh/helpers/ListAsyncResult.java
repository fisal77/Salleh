package com.seniorproject.salleh.helpers;

import java.util.List;

/**
 * Created by abdul on 09-Mar-2017.
 */

public interface ListAsyncResult<T> {
     void processFinish(List<T> result);

}
