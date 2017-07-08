package com.seniorproject.salleh.helpers;

import android.app.Application;

import com.seniorproject.salleh.entities.DomainPost;
import com.seniorproject.salleh.entities.DomainUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdul on 06-Apr-2017.
 */

public class MyApplication extends Application {
    public List<DomainUser> Friends_Cach =new ArrayList<>();
    public List<DomainPost> Posts_Cach = new ArrayList<>();



}
