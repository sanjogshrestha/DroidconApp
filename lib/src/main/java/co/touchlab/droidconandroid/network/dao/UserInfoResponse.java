package co.touchlab.droidconandroid.network.dao;

import java.util.List;

/**
 * Created by kgalligan on 4/6/16.
 */
public class UserInfoResponse
{
    public UserAccount     user;
    public List<EventInfo> speaking;
    public List<EventInfo> attending;
}
