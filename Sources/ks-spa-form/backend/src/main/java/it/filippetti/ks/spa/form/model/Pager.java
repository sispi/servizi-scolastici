/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.model;

import it.filippetti.ks.spa.form.exception.ValidationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 *
 * @author marco.mazzocchetti
 */
public class Pager {
    
    public static final int MAX_PAGE_SIZE = 4096;
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    private static final Map<Class<?>, Map<String, Sortable>> sortables = new HashMap<>();

    public static Pageable of(
        Class<?> type, 
        Integer pageNumber, 
        Integer pageSize) throws ValidationException {
        return of(type, pageNumber, pageSize, null);
    }

    public static Pageable of(
        Class<?> type,
        Integer pageNumber, 
        Integer pageSize, 
        String orderBy) throws ValidationException {
        
        boolean noPaging = false;
        Sort sort;
        
        // page number
        if (pageNumber == null) {
            noPaging = true;
            pageNumber = 1;
        }
        if (pageNumber <= 0) {
            throw new ValidationException("pageNumber: invalid value");
        }
        
        // page size
        if (pageSize == null) {
            pageSize = noPaging ? MAX_PAGE_SIZE : DEFAULT_PAGE_SIZE;
        } else {
            pageSize = Math.min(pageSize, MAX_PAGE_SIZE);
        }
        if (pageSize < 1) {
            throw new ValidationException("pageSize: invalid value");
        }
        
        // order by
        if (orderBy == null && type.getAnnotation(Sortables.class) != null) {
            orderBy = type.getAnnotation(Sortables.class).defaultSort();
        }
        if (StringUtils.isBlank(orderBy)) {
            sort = Sort.unsorted();
        } else {
            try {
                sort = Sort.by(Stream
                    .of(orderBy.split(",", -1))
                    .map(v -> {
                        String[] value;
                        Sortable sortable;
                        
                        value = v.split(":", -1);
                        if (value.length < 1 || value.length > 2 || StringUtils.isAnyBlank(value)) {
                            throw new IllegalArgumentException();
                        }
                        if ((sortable = sortables(type).get(value[0])) == null) {
                            throw new IllegalArgumentException(String.format(
                                "invalid property '%s', allowed values are %s", 
                                value[0],
                                sortables(type).keySet().stream().sorted().collect(Collectors.toList())));
                        }
                        if (value.length == 2) {
                            try {
                                return new Order(sortable, value[1]);
                            } catch (IllegalArgumentException e) {
                                throw new IllegalArgumentException(String.format(
                                    "invalid direction '%s', allowed values are %s", 
                                    value[1],
                                    Arrays.toString(Sort.Direction.values())));
                            }
                        }    
                        else {
                            return new Order(sortable, Sort.Direction.ASC);
                        }
                    })
                    .distinct()
                    .collect(Collectors.toList()));
            } catch (IllegalArgumentException e) {
                if (e.getMessage() != null) {
                    throw new ValidationException(String.format("orderBy: %s", e.getMessage()));
                } else {
                    throw new ValidationException("orderBy: invalid format");
                }
            }
        }
        return new Request(pageNumber, pageSize, sort);
    }
    
    /**
     * 
     * @param type
     * @return 
     */
    private static Map<String, Sortable> sortables(Class<?> type) {
        
        Map<String, Sortable> typeSortables;
        
        typeSortables = sortables.get(type);
        if (typeSortables == null) {
            typeSortables = new HashMap<>();
            if (type.getAnnotation(Sortables.class) != null) {
                for (Sortable sortable : type.getAnnotation(Sortables.class).value()) {
                    typeSortables.put(sortable.property(), sortable);
                }
            }
            sortables.put(type, typeSortables);
        }
        return typeSortables;
    }
    
    /**
     * 
     */
    public static class Request extends PageRequest {

        private Request(int page, int size, Sort sort) {
            super(page - 1, size, sort);
        }
        
        public int getOneBasedPageNumber() {
            return super.getPageNumber() + 1;
        }
    }
    
    /**
     * 
     */
    public static class Order extends Sort.Order {
    
        private Sortable sortable;
        
        private Order(Sortable sortable, String direction) {
            this(sortable, Sort.Direction.valueOf(direction.toUpperCase()));
        }
        
        private Order(Sortable sortable, Sort.Direction direction) {
            super(
                direction, 
                sortable.pathExpression().isEmpty() ? 
                    sortable.property() : 
                    sortable.pathExpression());
            this.sortable = sortable;
        }

        public Sortable getSortable() {
            return sortable;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.sortable.property());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Order other = (Order) obj;
            return Objects.equals(this.sortable.property(), other.sortable.property());
        }
    }
}
