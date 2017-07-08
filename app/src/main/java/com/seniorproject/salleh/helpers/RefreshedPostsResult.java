package com.seniorproject.salleh.helpers;

import com.seniorproject.salleh.entities.DomainPost;

import java.util.List;

/**
 * Created by abdul on 04-Apr-2017.
 */

public interface RefreshedPostsResult {
    void onGotResult(List<DomainPost> result);
}
