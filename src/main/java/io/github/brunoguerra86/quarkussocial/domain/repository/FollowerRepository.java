package io.github.brunoguerra86.quarkussocial.domain.repository;

import io.github.brunoguerra86.quarkussocial.domain.model.Follower;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
}
