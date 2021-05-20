package com.example.demo;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.ArrayList;
import java.util.List;

@Node
public class A {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Relationship(direction = Relationship.Direction.OUTGOING, type = "HAS")
    private List<B> bs  =  new ArrayList<>();

    public A() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<B> getBs() {
        return bs;
    }

    public void setBs(List<B> bs) {
        this.bs = bs;
    }

    @Override
    public String toString() {
        return "A{" +
                "id='" + id + '\'' +
                ", bs=" + bs +
                '}';
    }
}
