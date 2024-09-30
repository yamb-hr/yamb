package com.tejko.yamb.api.dto.responses;

import com.tejko.yamb.domain.enums.FriendRequestStatus;

public class FriendRequestResponse {

    private PlayerResponse friend;
    private FriendRequestStatus status;

    public FriendRequestResponse() {}

    public PlayerResponse getFriend() {
        return friend;
    }

    public void setFriend(PlayerResponse friend) {
        this.friend = friend;
    }

    public FriendRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FriendRequestStatus status) {
        this.status = status;
    }

}
