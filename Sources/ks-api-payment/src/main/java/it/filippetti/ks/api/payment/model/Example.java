/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 *
 * @author marco.mazzocchetti
 */
@Entity
@Table(
    name = "KS_EXAMPLE",
    indexes = {
        @Index(columnList = "tenant, organization")
    }
)
@Sortables(
    defaultSort = "name:ASC",
    value = {
        @Sortable(property = "name")
    }
)
@Fetchables(
    value = {
        @Fetchable(property = "parent", type = Example.class),
        @Fetchable(property = "children", type = Example.class)
    }
)
public class Example extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Basic
    @Column(nullable = false)
    private String tenant;

    @Basic
    @Column(nullable = false)
    private String organization;

    @Basic
    @Column(nullable = false)    
    private String name;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Example parent;

    @OrderBy("name ASC")
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Example> children;
    
    public Example() {
    }

    public Example(
        String tenant, 
        String organization,
        String name, 
        Example parent,
        List<String> children) {
        this.tenant = tenant;
        this.organization = organization;
        this.name = name;
        this.parent = parent;
        this.children = children
            .stream()
            .map(childName -> new Example(
                tenant, organization, childName, this, Collections.EMPTY_LIST))
            .collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getTenant() {
        return tenant;
    }

    public String getOrganization() {
        return organization;
    }

    public String getName() {
        return name;
    }

    public Example getParent() {
        return parent;
    }

    public List<Example> getChildren() {
        return children;
    }
}
