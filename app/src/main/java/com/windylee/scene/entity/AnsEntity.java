package com.windylee.scene.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by windy on 17/4/22.
 */

public class AnsEntity {

    @SerializedName("success")
    private boolean success;

    @SerializedName("user_id")
    private int userId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
