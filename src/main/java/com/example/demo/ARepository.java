package com.example.demo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ARepository extends Neo4jRepository<A, String> {
}
