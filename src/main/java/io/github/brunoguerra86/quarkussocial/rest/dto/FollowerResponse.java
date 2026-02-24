package io.github.brunoguerra86.quarkussocial.rest.dto;

import io.github.brunoguerra86.quarkussocial.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse() {

    }

    public FollowerResponse(Follower follower) {
        // We are using the id of the Follower entity, which is the id of the relationship between
        // the user and the follower, not the id of the follower user.
        this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
