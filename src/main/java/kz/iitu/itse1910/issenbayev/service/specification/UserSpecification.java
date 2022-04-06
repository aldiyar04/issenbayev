package kz.iitu.itse1910.issenbayev.service.specification;

import kz.iitu.itse1910.issenbayev.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component
//public class UserSpecification {
//    public Specification<User> get(String role, Boolean isDeveloperBusy) {
//        return (root, query, criteriaBuilder) -> {
//            List<Specification<User>> simpleSpecs = new ArrayList<>();
//            if (role != null) {
//                simpleSpecs.add(roleSpec(role));
//            }
//            if (isDeveloperBusy != null) {
//                simpleSpecs.add(isDeveloperBusySpec(isDeveloperBusy));
//            }
//        };
//    }
//
//    private Specification<User> roleSpec(String role) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.COLUMN_ROLE), role);
//    }
//
//    private Specification<User> isDeveloperBusySpec(Boolean isDeveloperBusy) {
//        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.COLUMN_ROLE), role);
//    }
//}
