package io.github.brunoguerra86.quarkussocial.rest;

import io.github.brunoguerra86.quarkussocial.domain.model.Follower;
import io.github.brunoguerra86.quarkussocial.domain.model.Post;
import io.github.brunoguerra86.quarkussocial.domain.model.User;
import io.github.brunoguerra86.quarkussocial.domain.repository.FollowerRepository;
import io.github.brunoguerra86.quarkussocial.domain.repository.PostRepository;
import io.github.brunoguerra86.quarkussocial.domain.repository.UserRepository;
import io.github.brunoguerra86.quarkussocial.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    @Inject
    PostRepository postRepository;;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUP() {
        // create a user to be used in the tests
        var user = new User();
        user.setName("Fulano");
        user.setAge(30);
        userRepository.persist(user);
        userId = user.getId();

        //create a post for the user
        Post post = new Post();
        post.setText("Hello world");
        post.setUser(user);
        postRepository.persist(post);

        //user that is not a follower of the user
        var userNotFollower = new User();
        userNotFollower.setName("Cicrano");
        userNotFollower.setAge(30);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //user that is a follower of the user
        var userFollower = new User();
        userFollower.setName("Cicrano");
        userFollower.setAge(30);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text for the post");

        var response =
                given()
                    .contentType(ContentType.JSON)
                    .body(postRequest)
                    .pathParam("userId", userId)
                .when()
                    .post()
                .then()
                    .statusCode(201);

    }

    @Test
    @DisplayName("should return 404 when trying to create a post for a non existing user")
    public void cretePostForAnInexistentUserTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text for the post");

        var inexistentUserId = 999;

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(postRequest)
                        .pathParam("userId", inexistentUserId)
                    .when()
                        .post()
                    .then()
                        .statusCode(404);

    }

    @Test
    @DisplayName("should return 403 when trying to create a post for a user that is not a follower")
    public void cretePostForAUserThatIsNotAFollowerTest() {
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some text for the post");

        var inexistentUserId = 999;

        var response =
                given()
                        .pathParam("userId", userId)
                        .header("followerId", userNotFollowerId)
                    .when()
                        .get()
                    .then()
                        .statusCode(403)
                        .body(Matchers.is("You can't see these posts"));

    }

    @Test
    @DisplayName("should return the posts ")
    public void listPostsTest() {
        given()
            .pathParam("userId", userId)
            .header("followerId", userFollowerId)
        .when()
            .get()
        .then()
            .statusCode(200)
            .body("size()", Matchers.is(1));
    }
}